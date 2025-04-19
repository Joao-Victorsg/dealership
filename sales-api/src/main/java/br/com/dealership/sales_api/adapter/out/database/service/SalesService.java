package br.com.dealership.sales_api.adapter.out.database.service;

import br.com.dealership.sales_api.adapter.mapper.SalesMapper;
import br.com.dealership.sales_api.adapter.out.database.repository.SalesRepository;
import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.exceptions.CarAlreadySoldException;
import br.com.dealership.sales_api.core.exceptions.SaleNotFoundException;
import br.com.dealership.sales_api.core.usecase.port.SalesServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

import static br.com.dealership.sales_api.adapter.out.database.repository.specification.SalesSpecificationsFactory.betweenDates;
import static br.com.dealership.sales_api.adapter.out.database.repository.specification.SalesSpecificationsFactory.hasCpf;

@RequiredArgsConstructor
@Service
public class SalesService implements SalesServicePort {

    private final SalesRepository salesRepository;
    private final SalesMapper salesMapper;

    @Override
    public SalesModel create(SalesModel salesModel) throws CarAlreadySoldException {
        if(salesRepository.existsByCarVin(salesModel.vin()))
            throw new CarAlreadySoldException("This car was already sold");

        final var entity = salesMapper.toEntity(salesModel);

        final var salesEntity = salesRepository.save(entity);

        return salesMapper.toModel(salesEntity);
    }

    @Override
    public void cancel(UUID salesId) throws SaleNotFoundException {
        if(salesRepository.findById(salesId).isEmpty())
            throw new SaleNotFoundException("There isn't a sale with this id");

        salesRepository.deleteById(salesId);
    }

    @Override
    public Page<SalesModel> searchAll(Pageable pageable, LocalDate initialDate, LocalDate finalDate, String cpf) {
        final var specification = Specification.where(betweenDates(initialDate,finalDate))
                .and(hasCpf(cpf));

        final var salesEntities = salesRepository.findAll(specification,pageable);

        final var salesModel = salesEntities.stream()
                .map(salesMapper::toModel)
                .toList();

        return new PageImpl<>(salesModel,salesEntities.getPageable(),salesModel.size());
    }

    @Override
    public SalesModel searchById(UUID salesId) throws SaleNotFoundException {
        final var salesEntity = salesRepository.findById(salesId)
                .orElseThrow(() -> new SaleNotFoundException("There isn't a sale with this id"));

        return salesMapper.toModel(salesEntity);
    }
}
