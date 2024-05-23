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

package SmartHome.Detectors;

public interface MoveDetectorPrx extends SmartHome.Interfaces.SmartDevicePrx,
                                         SmartHome.Interfaces.DetectorPrx
{
    default boolean motionDetected()
        throws SmartHome.Errors.ActionNotPermitted
    {
        return motionDetected(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default boolean motionDetected(java.util.Map<String, String> context)
        throws SmartHome.Errors.ActionNotPermitted
    {
        try
        {
            return _iceI_motionDetectedAsync(context, true).waitForResponseOrUserEx();
        }
        catch(SmartHome.Errors.ActionNotPermitted ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<java.lang.Boolean> motionDetectedAsync()
    {
        return _iceI_motionDetectedAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.Boolean> motionDetectedAsync(java.util.Map<String, String> context)
    {
        return _iceI_motionDetectedAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<java.lang.Boolean> _iceI_motionDetectedAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.Boolean> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "motionDetected", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_motionDetected);
        f.invoke(true, context, null, null, istr -> {
                     boolean ret;
                     ret = istr.readBool();
                     return ret;
                 });
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_motionDetected =
    {
        SmartHome.Errors.ActionNotPermitted.class
    };

    default void activateAlarm()
        throws SmartHome.Errors.ActionNotPermitted
    {
        activateAlarm(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default void activateAlarm(java.util.Map<String, String> context)
        throws SmartHome.Errors.ActionNotPermitted
    {
        try
        {
            _iceI_activateAlarmAsync(context, true).waitForResponseOrUserEx();
        }
        catch(SmartHome.Errors.ActionNotPermitted ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<Void> activateAlarmAsync()
    {
        return _iceI_activateAlarmAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Void> activateAlarmAsync(java.util.Map<String, String> context)
    {
        return _iceI_activateAlarmAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Void> _iceI_activateAlarmAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Void> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "activateAlarm", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_activateAlarm);
        f.invoke(true, context, null, null, null);
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_activateAlarm =
    {
        SmartHome.Errors.ActionNotPermitted.class
    };

    default void deactivateAlarm()
        throws SmartHome.Errors.ActionNotPermitted
    {
        deactivateAlarm(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default void deactivateAlarm(java.util.Map<String, String> context)
        throws SmartHome.Errors.ActionNotPermitted
    {
        try
        {
            _iceI_deactivateAlarmAsync(context, true).waitForResponseOrUserEx();
        }
        catch(SmartHome.Errors.ActionNotPermitted ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<Void> deactivateAlarmAsync()
    {
        return _iceI_deactivateAlarmAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Void> deactivateAlarmAsync(java.util.Map<String, String> context)
    {
        return _iceI_deactivateAlarmAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Void> _iceI_deactivateAlarmAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Void> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "deactivateAlarm", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_deactivateAlarm);
        f.invoke(true, context, null, null, null);
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_deactivateAlarm =
    {
        SmartHome.Errors.ActionNotPermitted.class
    };

    default void setAlarm(SmartHome.Interfaces.DailySchedule schedule)
        throws SmartHome.Errors.ActionNotPermitted
    {
        setAlarm(schedule, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default void setAlarm(SmartHome.Interfaces.DailySchedule schedule, java.util.Map<String, String> context)
        throws SmartHome.Errors.ActionNotPermitted
    {
        try
        {
            _iceI_setAlarmAsync(schedule, context, true).waitForResponseOrUserEx();
        }
        catch(SmartHome.Errors.ActionNotPermitted ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<Void> setAlarmAsync(SmartHome.Interfaces.DailySchedule schedule)
    {
        return _iceI_setAlarmAsync(schedule, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Void> setAlarmAsync(SmartHome.Interfaces.DailySchedule schedule, java.util.Map<String, String> context)
    {
        return _iceI_setAlarmAsync(schedule, context, false);
    }

    /**
     * @hidden
     * @param iceP_schedule -
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Void> _iceI_setAlarmAsync(SmartHome.Interfaces.DailySchedule iceP_schedule, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Void> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "setAlarm", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_setAlarm);
        f.invoke(true, context, null, ostr -> {
                     SmartHome.Interfaces.DailySchedule.ice_write(ostr, iceP_schedule);
                 }, null);
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_setAlarm =
    {
        SmartHome.Errors.ActionNotPermitted.class
    };

    default SmartHome.Interfaces.DailySchedule getAlarm()
        throws SmartHome.Errors.ActionNotPermitted
    {
        return getAlarm(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default SmartHome.Interfaces.DailySchedule getAlarm(java.util.Map<String, String> context)
        throws SmartHome.Errors.ActionNotPermitted
    {
        try
        {
            return _iceI_getAlarmAsync(context, true).waitForResponseOrUserEx();
        }
        catch(SmartHome.Errors.ActionNotPermitted ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<SmartHome.Interfaces.DailySchedule> getAlarmAsync()
    {
        return _iceI_getAlarmAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<SmartHome.Interfaces.DailySchedule> getAlarmAsync(java.util.Map<String, String> context)
    {
        return _iceI_getAlarmAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<SmartHome.Interfaces.DailySchedule> _iceI_getAlarmAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<SmartHome.Interfaces.DailySchedule> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "getAlarm", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_getAlarm);
        f.invoke(true, context, null, null, istr -> {
                     SmartHome.Interfaces.DailySchedule ret;
                     ret = SmartHome.Interfaces.DailySchedule.ice_read(istr);
                     return ret;
                 });
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_getAlarm =
    {
        SmartHome.Errors.ActionNotPermitted.class
    };

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static MoveDetectorPrx checkedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, ice_staticId(), MoveDetectorPrx.class, _MoveDetectorPrxI.class);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static MoveDetectorPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, context, ice_staticId(), MoveDetectorPrx.class, _MoveDetectorPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static MoveDetectorPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, ice_staticId(), MoveDetectorPrx.class, _MoveDetectorPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static MoveDetectorPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, context, ice_staticId(), MoveDetectorPrx.class, _MoveDetectorPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @return A proxy for this type.
     **/
    static MoveDetectorPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, MoveDetectorPrx.class, _MoveDetectorPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type.
     **/
    static MoveDetectorPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, facet, MoveDetectorPrx.class, _MoveDetectorPrxI.class);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the per-proxy context.
     * @param newContext The context for the new proxy.
     * @return A proxy with the specified per-proxy context.
     **/
    @Override
    default MoveDetectorPrx ice_context(java.util.Map<String, String> newContext)
    {
        return (MoveDetectorPrx)_ice_context(newContext);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the adapter ID.
     * @param newAdapterId The adapter ID for the new proxy.
     * @return A proxy with the specified adapter ID.
     **/
    @Override
    default MoveDetectorPrx ice_adapterId(String newAdapterId)
    {
        return (MoveDetectorPrx)_ice_adapterId(newAdapterId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoints.
     * @param newEndpoints The endpoints for the new proxy.
     * @return A proxy with the specified endpoints.
     **/
    @Override
    default MoveDetectorPrx ice_endpoints(com.zeroc.Ice.Endpoint[] newEndpoints)
    {
        return (MoveDetectorPrx)_ice_endpoints(newEndpoints);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator cache timeout.
     * @param newTimeout The new locator cache timeout (in seconds).
     * @return A proxy with the specified locator cache timeout.
     **/
    @Override
    default MoveDetectorPrx ice_locatorCacheTimeout(int newTimeout)
    {
        return (MoveDetectorPrx)_ice_locatorCacheTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the invocation timeout.
     * @param newTimeout The new invocation timeout (in seconds).
     * @return A proxy with the specified invocation timeout.
     **/
    @Override
    default MoveDetectorPrx ice_invocationTimeout(int newTimeout)
    {
        return (MoveDetectorPrx)_ice_invocationTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for connection caching.
     * @param newCache <code>true</code> if the new proxy should cache connections; <code>false</code> otherwise.
     * @return A proxy with the specified caching policy.
     **/
    @Override
    default MoveDetectorPrx ice_connectionCached(boolean newCache)
    {
        return (MoveDetectorPrx)_ice_connectionCached(newCache);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoint selection policy.
     * @param newType The new endpoint selection policy.
     * @return A proxy with the specified endpoint selection policy.
     **/
    @Override
    default MoveDetectorPrx ice_endpointSelection(com.zeroc.Ice.EndpointSelectionType newType)
    {
        return (MoveDetectorPrx)_ice_endpointSelection(newType);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for how it selects endpoints.
     * @param b If <code>b</code> is <code>true</code>, only endpoints that use a secure transport are
     * used by the new proxy. If <code>b</code> is false, the returned proxy uses both secure and
     * insecure endpoints.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default MoveDetectorPrx ice_secure(boolean b)
    {
        return (MoveDetectorPrx)_ice_secure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the encoding used to marshal parameters.
     * @param e The encoding version to use to marshal request parameters.
     * @return A proxy with the specified encoding version.
     **/
    @Override
    default MoveDetectorPrx ice_encodingVersion(com.zeroc.Ice.EncodingVersion e)
    {
        return (MoveDetectorPrx)_ice_encodingVersion(e);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its endpoint selection policy.
     * @param b If <code>b</code> is <code>true</code>, the new proxy will use secure endpoints for invocations
     * and only use insecure endpoints if an invocation cannot be made via secure endpoints. If <code>b</code> is
     * <code>false</code>, the proxy prefers insecure endpoints to secure ones.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default MoveDetectorPrx ice_preferSecure(boolean b)
    {
        return (MoveDetectorPrx)_ice_preferSecure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the router.
     * @param router The router for the new proxy.
     * @return A proxy with the specified router.
     **/
    @Override
    default MoveDetectorPrx ice_router(com.zeroc.Ice.RouterPrx router)
    {
        return (MoveDetectorPrx)_ice_router(router);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator.
     * @param locator The locator for the new proxy.
     * @return A proxy with the specified locator.
     **/
    @Override
    default MoveDetectorPrx ice_locator(com.zeroc.Ice.LocatorPrx locator)
    {
        return (MoveDetectorPrx)_ice_locator(locator);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for collocation optimization.
     * @param b <code>true</code> if the new proxy enables collocation optimization; <code>false</code> otherwise.
     * @return A proxy with the specified collocation optimization.
     **/
    @Override
    default MoveDetectorPrx ice_collocationOptimized(boolean b)
    {
        return (MoveDetectorPrx)_ice_collocationOptimized(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses twoway invocations.
     * @return A proxy that uses twoway invocations.
     **/
    @Override
    default MoveDetectorPrx ice_twoway()
    {
        return (MoveDetectorPrx)_ice_twoway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses oneway invocations.
     * @return A proxy that uses oneway invocations.
     **/
    @Override
    default MoveDetectorPrx ice_oneway()
    {
        return (MoveDetectorPrx)_ice_oneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch oneway invocations.
     * @return A proxy that uses batch oneway invocations.
     **/
    @Override
    default MoveDetectorPrx ice_batchOneway()
    {
        return (MoveDetectorPrx)_ice_batchOneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses datagram invocations.
     * @return A proxy that uses datagram invocations.
     **/
    @Override
    default MoveDetectorPrx ice_datagram()
    {
        return (MoveDetectorPrx)_ice_datagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch datagram invocations.
     * @return A proxy that uses batch datagram invocations.
     **/
    @Override
    default MoveDetectorPrx ice_batchDatagram()
    {
        return (MoveDetectorPrx)_ice_batchDatagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, except for compression.
     * @param co <code>true</code> enables compression for the new proxy; <code>false</code> disables compression.
     * @return A proxy with the specified compression setting.
     **/
    @Override
    default MoveDetectorPrx ice_compress(boolean co)
    {
        return (MoveDetectorPrx)_ice_compress(co);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection timeout setting.
     * @param t The connection timeout for the proxy in milliseconds.
     * @return A proxy with the specified timeout.
     **/
    @Override
    default MoveDetectorPrx ice_timeout(int t)
    {
        return (MoveDetectorPrx)_ice_timeout(t);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection ID.
     * @param connectionId The connection ID for the new proxy. An empty string removes the connection ID.
     * @return A proxy with the specified connection ID.
     **/
    @Override
    default MoveDetectorPrx ice_connectionId(String connectionId)
    {
        return (MoveDetectorPrx)_ice_connectionId(connectionId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except it's a fixed proxy bound
     * the given connection.@param connection The fixed proxy connection.
     * @return A fixed proxy bound to the given connection.
     **/
    @Override
    default MoveDetectorPrx ice_fixed(com.zeroc.Ice.Connection connection)
    {
        return (MoveDetectorPrx)_ice_fixed(connection);
    }

    static String ice_staticId()
    {
        return "::Detectors::MoveDetector";
    }
}
