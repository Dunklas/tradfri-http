package com.github.tradfrihttp.model;

import java.util.List;

public class LightGroup {

    public Long id;
    public String name;
    public List<Long> lightBulbs;

    public static class Builder {
        private Long id;
        private String name;
        private List<Long> lightBulbs;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLightBulbs(List<Long> lightBulbs) {
            this.lightBulbs = lightBulbs;
            return this;
        }

        public LightGroup build() {
            LightGroup lightGroup = new LightGroup();
            lightGroup.id = this.id;
            lightGroup.name = this.name;
            lightGroup.lightBulbs = this.lightBulbs;
            return lightGroup;
        }
    }
}
