#ifndef SMARTHOME_ICE
#define SMARTHOME_ICE

[["java:package:SmartHome", "js:es6-module"]]
module Enums {
    enum Mode {
        ACTIVE,
        RESTRICTED
    };

    enum Levels {
        LOW,
        MEDIUM,
        HIGH
    };
};

module Errors {
    exception ActionNotPermitted {};
    exception SensitivityLevelOutOfRange {};
    exception UnknownColor {};
    exception BrightnessLevelOutOfRange {};
};

module Interfaces {
    interface SmartDevice {
        idempotent void changeMode();
        idempotent Enums::Mode getMode();
        idempotent void notify() throws Errors::ActionNotPermitted;
    };

    interface Detector extends Interfaces::SmartDevice {
        idempotent void setSensitivityLevel(int level) throws Errors::ActionNotPermitted, Errors::SensitivityLevelOutOfRange;
        idempotent int getSensitivityLevel() throws  Errors::ActionNotPermitted;
        idempotent string getLocation() throws  Errors::ActionNotPermitted;
        idempotent void setLocation(string location) throws Errors::ActionNotPermitted;
    };

    interface LightControl extends Interfaces::SmartDevice {
        idempotent void turnOn() throws Errors::ActionNotPermitted;
        idempotent void turnOff() throws Errors::ActionNotPermitted;
        idempotent bool isOn() throws Errors::ActionNotPermitted;
    };

    struct DailySchedule {
        int startHour;
        int endHour;
    };

};

module Detectors {
    interface MoveDetector extends Interfaces::SmartDevice, Interfaces::Detector {
        idempotent bool motionDetected() throws Errors::ActionNotPermitted;
        idempotent void activateAlarm() throws Errors::ActionNotPermitted;
        idempotent void deactivateAlarm() throws Errors::ActionNotPermitted;
        idempotent void setAlarm(Interfaces::DailySchedule schedule) throws Errors::ActionNotPermitted;
        idempotent Interfaces::DailySchedule getAlarm() throws Errors::ActionNotPermitted;
    };

    interface SmokeDetector extends Interfaces::SmartDevice, Interfaces::Detector {
        idempotent int getSmokeDensity() throws  Errors::ActionNotPermitted;
        idempotent bool isSave() throws  Errors::ActionNotPermitted;
    };
};

module Lights{
    interface OutdoorLights extends  Interfaces::LightControl {
        idempotent void adjustBrightness(int level) throws Errors::ActionNotPermitted, Errors::BrightnessLevelOutOfRange;
        idempotent int getBrightnessLevel() throws Errors::ActionNotPermitted;
    };

    interface GeneralLights extends  Interfaces::LightControl {
        idempotent void changeColor(string color) throws Errors::ActionNotPermitted, Errors::UnknownColor;
        idempotent string getColor() throws Errors::ActionNotPermitted;
    };
};

#endif