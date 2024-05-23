#ifndef SMOKE_DETECTORS_HANDLER_H
#define SMOKE_DETECTORS_HANDLER_H

#include <iostream>
#include "./../SmartHome/SmartHome.h"
#include <Ice/Ice.h>
#include <glog/logging.h>
#include "DetectorsHandler.hpp"
#include "./../enums/SmokeDetectorEnum.hpp"

class SmokeDetectorsHandler: public DetectorsHandler{
private:
    Detectors::SmokeDetectorPrxPtr proxy;
public:
    explicit SmokeDetectorsHandler(const Ice::CommunicatorPtr& communicator) : DetectorsHandler::DetectorsHandler(communicator){};
    bool handleCommand(const std::string& command) override;
};

bool SmokeDetectorsHandler::handleCommand(const std::string &command) {
    switch (resolveSmokeDetectorCommand(command)) {
        case GET_SMOKE_DENSITY:
            LOG(INFO) << "SmokeDetectorsHandler - GET_SMOKE_DENSITY [ " <<  proxy->getSmokeDensity() << "]";
            return true;
        case IS_SAFE:
            LOG(INFO) << "SmokeDetectorsHandler - IS_SAFE [ " <<  proxy->isSave() << "]";
            return true;
        default:
            break;
    }

    return DetectorsHandler::handleCommand(command);
}

#endif