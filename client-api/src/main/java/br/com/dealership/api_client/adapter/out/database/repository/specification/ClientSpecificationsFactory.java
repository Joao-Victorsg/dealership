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
        return ((root, query, builder) ->
                city == null ? null : builder.equal(builder.lower(root.get("address").get("city")),city.toLowerCase()));
    }

    public static Specification<ClientEntity> hasState(String stateAbbreviation){
        return ((root, query, builder) ->
                stateAbbreviation == null ? null :
                        builder.equal(builder.lower(root.get("address").get("stateAbbreviation")),stateAbbreviation.toLowerCase()));
    }
}