package org.j1p5.domain.product.repository.querydsl;

import java.util.List;
import org.j1p5.domain.product.entity.ProductEntity;
import org.locationtech.jts.geom.Point;

public interface ProductRepositoryCustom {

    List<ProductEntity> findProductsByCursor(Point coordinate, Long cursor, Integer size);
}
