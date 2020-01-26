package com.github.tradfrihttp.model;

public class LightBulb {

    private int id;
    private String name;
    private String deviceTypeName;
    private String deviceTypeId;
    private boolean powerOn;
    private int dimmer;
    private int xColor;
    private int yColor;
    private String hexColor;

    private LightBulb() {}

    public static class Builder {

        private int id;
        private String name;
        private String deviceTypeName;
        private String deviceTypeId;
        private boolean powerOn;
        private int dimmer;
        private int xColor;
        private int yColor;
        private String hexColor;

        public Builder() {}

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDeviceTypeName(String deviceTypeName) {
            this.deviceTypeName = deviceTypeName;
            return this;
        }

        public Builder setDeviceTypeId(String deviceTypeId) {
            this.deviceTypeId = deviceTypeId;
            return this;
        }

        public Builder setPowerOn(boolean powerOn) {
            this.powerOn = powerOn;
            return this;
        }

        public Builder setDimmer(int dimmer) {
            this.dimmer = dimmer;
            return this;
        }

        public Builder setxColor(int xColor) {
            this.xColor = xColor;
            return this;
        }

        public Builder setyColor(int yColor) {
            this.yColor = yColor;
            return this;
        }

        public Builder setHexColor(String hexColor) {
            this.hexColor = hexColor;
            return this;
        }

        public LightBulb build() {
            LightBulb lightBulb = new LightBulb();
            lightBulb.id = this.id;
            lightBulb.name = this.name;
            lightBulb.deviceTypeName = this.deviceTypeName;
            lightBulb.deviceTypeId = this.deviceTypeId;
            lightBulb.powerOn = this.powerOn;
            lightBulb.dimmer = this.dimmer;
            lightBulb.xColor = this.xColor;
            lightBulb.yColor = this.yColor;
            lightBulb.hexColor = this.hexColor;
            return lightBulb;
        }
    }
}
