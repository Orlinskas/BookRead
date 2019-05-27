package com.orlinskas.bookread.helpers;

import android.content.Context;

import com.orlinskas.bookread.Licence;
import com.orlinskas.bookread.data.LicenceData;

public class LicenceHelper {
    private Context context;

    public LicenceHelper(Context context){
        this.context = context;
    }

    public void update () {
        LicenceData licenceData = new LicenceData(context);
        Licence licence = licenceData.loadLicence();
        licence.newBookCreate();
        licenceData.saveLicence(licence);
    }
}
