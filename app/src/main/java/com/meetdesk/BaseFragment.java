package com.meetdesk;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ekobudiarto on 9/15/16.
 */
public class BaseFragment extends Fragment {

    String TAG;

    Map<String, String> parameter;
    ArrayList<BaseFragment> target;
    boolean isSliding = false;

    public BaseFragment()
    {

    }

    public void setFragmentTAG(String tag)
    {
        this.TAG = tag;
    }

    public String getFragmentTAG()
    {
        return this.TAG;
    }

    public void setParameter(Map<String, String> param)
    {
        this.parameter = param;
    }

    public Map<String, String> getParameter()
    {
        return this.parameter;
    }

    public void setSlidingMenu(boolean slidingMenu)
    {
        this.isSliding = slidingMenu;
    }

    public boolean getSlidingMenu()
    {
        return this.isSliding;
    }

    public void onUpdate()
    {

    }

    public void setFragmentUpdate(ArrayList<BaseFragment> target)
    {
        this.target = target;
    }

    public ArrayList<BaseFragment> getFragmentUpdate()
    {
        return this.target;
    }
}
