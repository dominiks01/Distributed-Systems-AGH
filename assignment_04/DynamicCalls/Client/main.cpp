#include <iostream>
#include <Ice/Ice.h>

struct item {
    int a;
    int b;
};

void handleAddCall(const Ice::CommunicatorPtr& ic, int x, int y, const std::string& proxy){
    Ice::ObjectPrx base = ic->stringToProxy(proxy);
    {
        std::vector<Ice::Byte> inParams, outParams;
        Ice::OutputStream out(ic);
        out.startEncapsulation();
        out.write(x);
        out.write(y);
        out.endEncapsulation();
        out.finished(inParams);

        if(base->ice_invoke("add", Ice::Normal, inParams, outParams))
        {
            Ice::InputStream in(ic, outParams);
            in.startEncapsulation();
            int result;
            in.read(result);
            in.endEncapsulation();
            assert(result == x + y);

            std::cout << "ADD: Call function success \n";
        }
        else
        {
            std::cout << "ADD: Call function failure \n ";
        }
    }
}

void handleSubtractCall(Ice::CommunicatorPtr& ic, int x, int y, const std::string& proxy){
    Ice::ObjectPrx base = ic->stringToProxy(proxy);
    {
        std::vector<Ice::Byte> inParams, outParams;
        Ice::OutputStream out(ic);
        out.clear();
        out.startEncapsulation();
        x = 100, y = -1;
        out.write(x);
        out.write(y);
        out.endEncapsulation();
        out.finished(inParams);

        if (base->ice_invoke("subtract", Ice::Normal, inParams, outParams)) {
            Ice::InputStream in(ic, outParams);
            in.startEncapsulation();
            int result;
            in.read(result);
            in.endEncapsulation();
            std::cout << "SUBTRACT: Call function success \n";
        } else {
            std::cout << "SUBTRACT: Call function success \n";
        }
    }
}

void handlePrint(Ice::CommunicatorPtr& ic, const std::vector<int>& values, const std::string& proxy){
    Ice::ObjectPrx base = ic->stringToProxy(proxy);
    {
        std::vector<Ice::Byte> inParams, outParams;
        Ice::OutputStream out(ic);
        out.startEncapsulation();
        out.write(&values[0], &values[values.size()]);
        out.endEncapsulation();
        out.finished(inParams);

        if(base->ice_invoke("print", Ice::Normal, inParams, outParams))
        {
            std::cout << "PRINT: Call function success \n";
        }
        else
        {
            std::cout << "PRINT: Call function failure \n ";
        }
    }
}

int main(int argc, char* argv[]) {
    int status = 0;
    Ice::CommunicatorPtr ic;

    std::string proxy("DynamicCalls:default -p 10000");

    try {
        ic = Ice::initialize(argc, argv);

        handleAddCall(ic, 1, 2, proxy);
        handleSubtractCall(ic, 1, 2,proxy);
        handlePrint(ic, std::vector<int>{1,2,3,4,5}, proxy);

    } catch (const Ice::Exception& ex) {
        std::cerr << ex << std::endl;
        status = 1;
    } catch (const char* msg) {
        std::cerr << msg << std::endl;
        status = 1;
    }
    if (ic)
        ic->destroy();
    return status;
}
