#include <iostream>
#include <sstream>
#include <glog/logging.h>
#include <Ice/Ice.h>
#include "SmartHome/SmartHome.h"

int main(int argc, char* argv[]) {
    Ice::CommunicatorPtr ic;
//    int status = 0;
//    Ice::CommunicatorPtr ic;
//
//    try {
//        ic = Ice::initialize(argc, argv);
//        Ice::ObjectPrx base = ic->stringToProxy("SD1:default -p 10000");
//        Detectors::SmokeDetectorPrx smoke = Detectors::SmokeDetectorPrx::checkedCast(base);
//        if (!smoke)
//            throw "Invalid proxy";
//
//        smoke->setSensitivityLevel(2);
//    } catch (const Ice::Exception& ex) {
//        std::cerr << ex << std::endl;
//        status = 1;
//    } catch (const char* msg) {
//        std::cerr << msg << std::endl;
//        status = 1;
//    }
//    if (ic)
//        ic->destroy();
//    return status;
    return 0;
}
