package com.sktlab.android.base.key;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.security.auth.x500.X500Principal;

public class KeyHelper {
    private static final String TAG = KeyHelper.class.getSimpleName();
    private static final String CURVE_NAME = "secp256r1";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String ALGORITHM_SIGN = "SHA256withECDSA";

    public static boolean generateKeypairInKeyStore(String alias) {
        if (keyStoreContainsAlias(alias)) {
            Log.e(TAG, "The alias already include in key store:" + alias);
            return false;
        }
        try {
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(CURVE_NAME);
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                    .setAlgorithmParameterSpec(ecSpec)
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setCertificateSubject(new X500Principal("CN=" + alias))
                    .build();
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, ANDROID_KEY_STORE);
            generator.initialize(spec);
            generator.generateKeyPair();
            return true;
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
            e.printStackTrace();
            Log.e(TAG, "generate keypair exception " + e.toString());
        }
        return false;
    }

    public static ECPublicKey getPublicKeyInKeyStore(String alias) {
        if (keyStoreContainsAlias(alias)) {
            try {
                return (ECPublicKey) getKeyStore().getCertificate(alias).getPublicKey();
            } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
                Log.e(TAG, "get public key exception " + e.toString());
            }
        }
        return null;
    }

    public static byte[] signInKeyStore(String alias, byte[] data) {
        if (keyStoreContainsAlias(alias)) {
            try {
                KeyStore keyStore = getKeyStore();
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);
                Signature sign = Signature.getInstance(ALGORITHM_SIGN);
                sign.initSign(privateKey);
                sign.update(data);
                return sign.sign();
            } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | InvalidKeyException | SignatureException | UnrecoverableEntryException e) {
                e.printStackTrace();
                Log.e(TAG, "sign exception " + e.toString());
            }
        }
        return null;
    }

    public static boolean verifyInKeyStore(String alias, byte[] data, byte[] signature) {
        if (keyStoreContainsAlias(alias)) {
            try {
                Signature sign = Signature.getInstance(ALGORITHM_SIGN);
                sign.initVerify(getKeyStore().getCertificate(alias));
                sign.update(data);
                return sign.verify(signature);
            } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | InvalidKeyException | SignatureException e) {
                e.printStackTrace();
                Log.e(TAG, "sign exception " + e.toString());
            }
        }
        return false;
    }

    public static boolean deleteInKeyStore(String alias) {
        if (keyStoreContainsAlias(alias)) {
            try {
                KeyStore keyStore = getKeyStore();
                keyStore.deleteEntry(alias);
                return true;
            } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
                Log.e(TAG, "delete alias exception " + e.toString());
            }
        }
        return false;
    }

    public static boolean keyStoreContainsAlias(String alias) {
        try {
            return getKeyStore().containsAlias(alias);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            Log.e(TAG, "contain alias exception " + e.toString());
        }
        return false;
    }

    private static KeyStore getKeyStore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
        return keyStore;
    }

    public static boolean verify(byte[] publicKey, byte[] data, byte[] signature) {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
        try {
            KeyFactory kf = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_EC);
            Signature sign = Signature.getInstance(ALGORITHM_SIGN);
            sign.initVerify(kf.generatePublic(spec));
            sign.update(data);
            return sign.verify(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }
}
