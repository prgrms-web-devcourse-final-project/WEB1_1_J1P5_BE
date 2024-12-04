package org.j1p5.domain.activityArea.repository.querydsl;

import org.j1p5.domain.activityArea.dto.ActivityAreaAddress;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityAreaRepositoryCustom {
    Page<ActivityAreaAddress> getActivityAreas(Point coordinate, Pageable pageable);
}