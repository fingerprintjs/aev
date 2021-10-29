package com.fingerprintjs.android.application_protector.raw_signal_providers


import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate


class CertificateInfo(
    val dnameList: List<String>,
    val sigAlgInfo: List<String>
)

interface PackageManagerInfoProvider {
    fun getCertificateInfo(packageName: String): CertificateInfo
}

class PackageManagerInfoProviderImpl(
    private val packageManager: PackageManager
) : PackageManagerInfoProvider {
    override fun getCertificateInfo(packageName: String): CertificateInfo {
        val certificates = getSigningCertificates(packageName)
        return CertificateInfo(
            certificates.map { it.issuerDN.name },
            certificates.map { it.sigAlgName }
        )
    }

    @SuppressLint("PackageManagerGetSignatures")
    private fun getSigningCertificates(packageName: String): List<X509Certificate> {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            packageInfo.signatures.mapNotNull {
                extractX509FromSignature(it.toByteArray())
            }
        } else {
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            packageInfo.signingInfo?.apkContentsSigners?.mapNotNull { signature ->
                extractX509FromSignature(signature.toByteArray())
            }
        } ?: emptyList()
    }

    private fun extractX509FromSignature(byteArray: ByteArray): X509Certificate {
        val certStream: InputStream = ByteArrayInputStream(byteArray)
        val certFactory = CertificateFactory.getInstance("X509")
        return certFactory.generateCertificate(certStream) as X509Certificate
    }
}