#ifndef MY_COMMAND_HANLDER
#define MY_COMMAND_HANLDER

#include <iostream>
#include <Ice/Ice.h>
#include "./../SmartHome/SmartHome.h"

class CommandHandler {
private:
    std::string PROXY_NAME_SUFFIX = ".Proxy";
protected:
    Ice::CommunicatorPtr ic;
    Ice::ObjectPrx getProxy(const std::string& proxyName);
    virtual bool handleCommand(const std::string& command);
public:
    explicit CommandHandler(const Ice::CommunicatorPtr& communicator);
};

Ice::ObjectPrx CommandHandler::getProxy(const std::string& proxyName) {
    return ic->propertyToProxy(proxyName + PROXY_NAME_SUFFIX);
}

bool CommandHandler::handleCommand(const std::string &command) {
    return false;
}

CommandHandler::CommandHandler(const Ice::CommunicatorPtr& communicator) {
    this->ic = communicator;
}

#endif