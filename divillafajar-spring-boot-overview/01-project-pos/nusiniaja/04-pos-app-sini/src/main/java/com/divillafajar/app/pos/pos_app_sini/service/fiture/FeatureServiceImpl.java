package com.divillafajar.app.pos.pos_app_sini.service.fiture;

import com.divillafajar.app.pos.pos_app_sini.repo.feature.FeatureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService{

    private final FeatureRepo featureRepo;

    @Override
    public void ensureBasicFeature() {
        featureRepo.insertIfNotExists("Basic");
    }

    @Override
    public Long getBasicFeatureId() {
        return 0L;
    }
}
