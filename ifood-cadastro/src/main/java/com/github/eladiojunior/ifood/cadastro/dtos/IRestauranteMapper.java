package com.github.eladiojunior.ifood.cadastro.dtos;

import com.github.eladiojunior.ifood.cadastro.entites.Prato;
import com.github.eladiojunior.ifood.cadastro.entites.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface IRestauranteMapper {

    @Mapping(target = "dataHoraRegistro", dateFormat = "dd/MM/yyyy H:mm:ss")
    RestauranteDTO toRestauranteDTO(Restaurante entity);

    Restaurante toRestaurante(AdicionarRestauranteDTO dto);
    void toRestaurante(AtualizarRestauranteDTO dto, @MappingTarget Restaurante entity);

    PratoDTO toPratoDTO(Prato entity);
    Prato toPrato(AdicionarPratoDTO dto);
    void toPrato(AtualizarPratoDTO dto, @MappingTarget Prato entity);

}
