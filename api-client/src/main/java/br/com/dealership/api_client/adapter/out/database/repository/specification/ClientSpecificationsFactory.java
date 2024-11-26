package br.com.dealership.api_client.adapter.out.database.repository.specification;

import br.com.dealership.api_client.adapter.out.database.entity.ClientEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;

@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientSpecificationsFactory {

    public static Specification<ClientEntity> hasCity(String city){
        return ((root, query, criteriaBuilder) ->
                city == null ? null : criteriaBuilder.equal(root.get("address").get("city"),city));
    }

    public static Specification<ClientEntity> hasState(String stateAbbreviation){
        return ((root, query, criteriaBuilder) ->
                stateAbbreviation == null ? null :
                        criteriaBuilder.equal(root.get("address").get("stateAbbreviation"),stateAbbreviation));
    }
}