package com.github.tradfrihttp.model;

import java.util.List;

public class LightGroup {

    public int id;
    public String name;
    public List<Integer> lightBulbs;

    public static class Builder {
        private int id;
        private String name;
        private List<Integer> lightBulbs;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLightBulbs(List<Integer> lightBulbs) {
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
