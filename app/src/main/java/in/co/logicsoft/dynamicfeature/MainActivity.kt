package `in`.co.logicsoft.dynamicfeature

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_main.*

private const val FEATURE_1 = "in.co.logicsoft.feature1.ActivityFeature1"

class MainActivity : BaseSplitActivity() {
    var mySessionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Creates an instance of SplitInstallManager.
        val splitInstallManager = SplitInstallManagerFactory.create(this)

        val request = SplitInstallRequest
            .newBuilder()
            .addModule("feature1")
            .build()

        val listener = SplitInstallStateUpdatedListener { state ->
            if (state.sessionId() == mySessionId) {
                when (state.status()) {
                    SplitInstallSessionStatus.DOWNLOADING -> {
                        status.text = "DOWNLOADING"
                        val totalBytes = state.totalBytesToDownload()
                        val progress = state.bytesDownloaded()
                        progressBar.max = totalBytes.toInt()
                        progressBar.progress = progress.toInt()
                    }
                    SplitInstallSessionStatus.INSTALLING -> {
                        status.text = "INSTALLING"
                    }
                    SplitInstallSessionStatus.INSTALLED -> {
                        status.text = "INSTALLED"
                        Intent().setClassName(BuildConfig.APPLICATION_ID, FEATURE_1).also {
                            startActivity(it)
                        }
                    }

                    SplitInstallSessionStatus.FAILED -> {
                        status.text = "FAILED" + state.errorCode()
                    }
                    SplitInstallSessionStatus.CANCELING -> {
                        status.text = "CANCELING" + state.errorCode()
                    }
                    SplitInstallSessionStatus.CANCELED -> {
                        status.text = "CANCELING" + state.errorCode()
                    }
                }
            }
        }
        splitInstallManager.registerListener(listener)

        button.setOnClickListener {
            if (splitInstallManager.installedModules.contains("feature1")) {
                Intent().setClassName(BuildConfig.APPLICATION_ID, FEATURE_1).also {
                    startActivity(it)
                }
            } else {
                splitInstallManager.startInstall(request)
                    .addOnSuccessListener { sessionId ->
                        mySessionId = sessionId
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}
