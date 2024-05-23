#ifndef SMARTHOME_ICE
#define SMARTHOME_ICE

[["java:package:SmartHome"]]
module Enums {
    enum Mode {
        Active,
        Restricted
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
        idempotent void setLocation(string location) throws  Errors::ActionNotPermitted;
    };

    interface LightControl extends Interfaces::SmartDevice {
        idempotent void turnOn() throws Errors::ActionNotPermitted;
        idempotent void turnOff() throws Errors::ActionNotPermitted;
        idempotent bool isOn() throws Errors::ActionNotPermitted;
    };
};

module Detectors {
    interface MoveDetector extends Interfaces::SmartDevice, Interfaces::Detector {
        idempotent bool motionDetected() throws  Errors::ActionNotPermitted;
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

//
//module SmartHome {
//
//        // Device Mode
//        enum Mode {
//            Active,
//            Restricted
//        };
//
//        // If device is in restricted mode
//        exception ActionNotPermitted {};
//
//        // Interface for SmartHome
//        interface SmartDevice {
//            idempotent void changeMode();
//            idempotent Mode::Mode getMode();
//            idempotent void notify() throws ActionNotPermitted;
//        };
//
//        exception SensitivityLevelOutOfRange {};
//
//        interface Detector extends SmartDevice{
//             idempotent void setSensitivityLevel(int level) throws ActionNotPermitted, SensitivityLevelOutOfRange;
//             idempotent int getSensitivityLevel() throws ActionNotPermitted;
//             idempotent string getLocation() throws ActionNotPermitted;
//             idempotent void setLocation(string location) throws ActionNotPermitted;
//           };
//
//        interface MoveDetector extends SmartDevice, Detector {
//            idempotent bool motionDetected() throws ActionNotPermitted;
//        };
//
//        interface SmokeDetector extends SmartDevice, Detector {
//            idempotent int getSmokeDensity() throws ActionNotPermitted;
//            idempotent bool isSave() throws ActionNotPermitted;
//        };
//
//        interface LightControl extends SmartDevice {
//            idempotent void turnOn() throws ActionNotPermitted;
//            idempotent void turnOff() throws ActionNotPermitted;
//            idempotent bool isOn() throws ActionNotPermitted;
//        };
//
//        exception UnknownColor {};
//        exception BrightnessLevelOutOfRange {};
//
//        interface OutdoorLights extends LightControl {
//            idempotent void adjustBrightness(int level) throws ActionNotPermitted, BrightnessLevelOutOfRange;
//            idempotent int getBrightnessLevel() throws ActionNotPermitted;
//        };
//
//         interface GeneralLights extends LightControl {
//            idempotent void changeColor(string color) throws ActionNotPermitted, UnknownColor;
//            idempotent string getColor() throws ActionNotPermitted;
//         };
//
//};

#endif