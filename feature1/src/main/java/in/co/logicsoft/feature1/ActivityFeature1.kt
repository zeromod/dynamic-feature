package `in`.co.logicsoft.feature1

import android.os.Bundle
import `in`.co.logicsoft.dynamicfeature.BaseSplitActivity

class ActivityFeature1 : BaseSplitActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature1)
    }
}
