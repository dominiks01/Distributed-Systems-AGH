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

package SmartHome.Interfaces;

public class DailySchedule implements java.lang.Cloneable,
                                      java.io.Serializable
{
    public int startHour;

    public int endHour;

    public DailySchedule()
    {
    }

    public DailySchedule(int startHour, int endHour)
    {
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        DailySchedule r = null;
        if(rhs instanceof DailySchedule)
        {
            r = (DailySchedule)rhs;
        }

        if(r != null)
        {
            if(this.startHour != r.startHour)
            {
                return false;
            }
            if(this.endHour != r.endHour)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::Interfaces::DailySchedule");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, startHour);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, endHour);
        return h_;
    }

    public DailySchedule clone()
    {
        DailySchedule c = null;
        try
        {
            c = (DailySchedule)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeInt(this.startHour);
        ostr.writeInt(this.endHour);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.startHour = istr.readInt();
        this.endHour = istr.readInt();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, DailySchedule v)
    {
        if(v == null)
        {
            _nullMarshalValue.ice_writeMembers(ostr);
        }
        else
        {
            v.ice_writeMembers(ostr);
        }
    }

    static public DailySchedule ice_read(com.zeroc.Ice.InputStream istr)
    {
        DailySchedule v = new DailySchedule();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<DailySchedule> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, DailySchedule v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.VSize))
        {
            ostr.writeSize(8);
            ice_write(ostr, v);
        }
    }

    static public java.util.Optional<DailySchedule> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.VSize))
        {
            istr.skipSize();
            return java.util.Optional.of(DailySchedule.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final DailySchedule _nullMarshalValue = new DailySchedule();

    /** @hidden */
    public static final long serialVersionUID = -5428867779009493618L;
}