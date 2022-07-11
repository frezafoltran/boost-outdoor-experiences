package com.foltran.feature_experience.navigation

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import com.foltran.core_navigation.Routable
import com.foltran.feature_experience.core.presentation.ExperienceActivity


class ExperienceRouter {

    companion object :
        Routable<ExperienceActivity, ExperienceRouterData> {
        override val route: Class<ExperienceActivity>
            get() = ExperienceActivity::class.java
        override val deepLinkAuthority: String
            get() = ExperienceRouterData.ROUTE_AUTHORITY
        override val routerDataClass: Class<ExperienceRouterData>
            get() = ExperienceRouterData::class.java

        private const val DEFAULT_BEATLE = "john"
        private const val DEEP_LINK_TAG = "experience"

        override fun startActivity(context: Context?, data: Parcelable?) {

            buildIntent(context).putExtra(Routable.ROUTE_INTENT_DATA,
                data?.takeIf { it.javaClass == routerDataClass }
                    ?: ExperienceRouterData(DEFAULT_BEATLE)
            ).also {
                context?.startActivity(it)
            }
        }

        override fun startDeepLink(context: Context?, data: Uri?) {
            startActivity(context, ExperienceRouterData(data?.getQueryParameter(DEEP_LINK_TAG)))
        }
    }
}