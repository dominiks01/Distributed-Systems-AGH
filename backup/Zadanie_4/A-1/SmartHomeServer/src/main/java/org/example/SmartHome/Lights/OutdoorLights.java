//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.10
//
// <auto-generated>
//
// Generated from file `SmartHome.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package SmartHome.Lights;

public interface OutdoorLights extends SmartHome.Interfaces.LightControl
{
    void adjustBrightness(int level, com.zeroc.Ice.Current current)
        throws SmartHome.Errors.ActionNotPermitted,
               SmartHome.Errors.BrightnessLevelOutOfRange;

    int getBrightnessLevel(com.zeroc.Ice.Current current)
        throws SmartHome.Errors.ActionNotPermitted;

    /** @hidden */
    static final String[] _iceIds =
    {
        "::Ice::Object",
        "::Interfaces::LightControl",
        "::Interfaces::SmartDevice",
        "::Lights::OutdoorLights"
    };

    @Override
    default String[] ice_ids(com.zeroc.Ice.Current current)
    {
        return _iceIds;
    }

    @Override
    default String ice_id(com.zeroc.Ice.Current current)
    {
        return ice_staticId();
    }

    static String ice_staticId()
    {
        return "::Lights::OutdoorLights";
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_adjustBrightness(OutdoorLights obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(com.zeroc.Ice.OperationMode.Idempotent, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        int iceP_level;
        iceP_level = istr.readInt();
        inS.endReadParams();
        obj.adjustBrightness(iceP_level, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getBrightnessLevel(OutdoorLights obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(com.zeroc.Ice.OperationMode.Idempotent, current.mode);
        inS.readEmptyParams();
        int ret = obj.getBrightnessLevel(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeInt(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    /** @hidden */
    final static String[] _iceOps =
    {
        "adjustBrightness",
        "changeMode",
        "getBrightnessLevel",
        "getMode",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "isOn",
        "notify",
        "turnOff",
        "turnOn"
    };

    /** @hidden */
    @Override
    default java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceDispatch(com.zeroc.IceInternal.Incoming in, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        int pos = java.util.Arrays.binarySearch(_iceOps, current.operation);
        if(pos < 0)
        {
            throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return _iceD_adjustBrightness(this, in, current);
            }
            case 1:
            {
                return SmartHome.Interfaces.SmartDevice._iceD_changeMode(this, in, current);
            }
            case 2:
            {
                return _iceD_getBrightnessLevel(this, in, current);
            }
            case 3:
            {
                return SmartHome.Interfaces.SmartDevice._iceD_getMode(this, in, current);
            }
            case 4:
            {
                return com.zeroc.Ice.Object._iceD_ice_id(this, in, current);
            }
            case 5:
            {
                return com.zeroc.Ice.Object._iceD_ice_ids(this, in, current);
            }
            case 6:
            {
                return com.zeroc.Ice.Object._iceD_ice_isA(this, in, current);
            }
            case 7:
            {
                return com.zeroc.Ice.Object._iceD_ice_ping(this, in, current);
            }
            case 8:
            {
                return SmartHome.Interfaces.LightControl._iceD_isOn(this, in, current);
            }
            case 9:
            {
                return SmartHome.Interfaces.SmartDevice._iceD_notify(this, in, current);
            }
            case 10:
            {
                return SmartHome.Interfaces.LightControl._iceD_turnOff(this, in, current);
            }
            case 11:
            {
                return SmartHome.Interfaces.LightControl._iceD_turnOn(this, in, current);
            }
        }

        assert(false);
        throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
    }
}
