package com.sukinsan.koshot.controller;


import com.sukinsan.koshot.entity.ShotEntity;

import java.util.List;

public class ShotsResponse {
    private List<ShotEntity> shots;

    public ShotsResponse(List<ShotEntity> files) {
        this.shots = files;
    }

    public List<ShotEntity> getShots() {
        return shots;
    }

    public void setShots(List<ShotEntity> shots) {
        this.shots = shots;
    }

    @Override
    public String toString() {
        return "ShotsResponse{" +
                "shots=" + shots +
                '}';
    }
}
