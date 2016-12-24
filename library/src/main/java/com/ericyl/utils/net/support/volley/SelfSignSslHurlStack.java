package com.ericyl.utils.net.support.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.HurlStack;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class SelfSignSslHurlStack extends HurlStack {

    public SelfSignSslHurlStack(Context context, String sslType, String sslPath) {
        super(null, getSslSocketFactory(context, sslType, sslPath));
    }

    public SelfSignSslHurlStack(SSLSocketFactory sslSocketFactory) {
        super(null, sslSocketFactory);
    }

    /**
     * getSslSocketFactory
     * @param content assets
     * @param sslType SSL Type like{"X.509", "BKS"}
     * @param sslPath cert name
     * @return SSLSocketFactory
     * @hide
     */
    private static SSLSocketFactory getSslSocketFactory(Context content, String sslType, String sslPath) {
        try {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or...)
            CertificateFactory cf = CertificateFactory.getInstance(sslType);
            InputStream caInput = content.getResources().getAssets().open(sslPath);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                Log.v("ca Info:", "" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}

