package com.topsec.sslvpn.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

    static class C03181 implements FilenameFilter {
        private final /* synthetic */ String val$strFilter;

        C03181(String str) {
            this.val$strFilter = str;
        }

        public boolean accept(File fDir, String strName) {
            if (this.val$strFilter == null || "" == this.val$strFilter || -1 < strName.indexOf(this.val$strFilter)) {
                return true;
            }
            return false;
        }
    }

    public static synchronized void ZipFile(File[] fSrcList, String strZipFile, boolean bDelAfterProc) throws Throwable {
        Throwable th;
        FileOutputStream fosNewTmp = null;
        synchronized (ZipHelper.class) {
            FileOutputStream fosNewTmp2 = null;
            ZipOutputStream zosNewTmp = null;
            ZipFile zfTmp = null;
            File fNewFile = null;
            File fTmpFile = null;
            try {
                File fNewFile2 = new File(strZipFile);
                try {
                    if (fNewFile2.exists()) {
                        File fTmpFile2 = new File(new StringBuilder(String.valueOf(strZipFile)).append(".tmp").toString());
                        try {
                            fNewFile2.renameTo(fTmpFile2);
                            fTmpFile = fTmpFile2;
                            zfTmp = new ZipFile(fTmpFile2);
                        } catch (Exception e) {
                            fTmpFile = fTmpFile2;
                            fNewFile = fNewFile2;
                            if (fNewFile != null) {
                                try {
                                    fNewFile.delete();
                                } catch (Throwable th2) {
                                    th = th2;
                                    if (zosNewTmp != null) {
                                        try {
                                            zosNewTmp.finish();
                                            zosNewTmp.close();
                                        } catch (IOException e2) {
                                            if (fTmpFile != null) {
                                                fTmpFile.delete();
                                            }
                                            throw th;
                                        }
                                    }
                                    if (fosNewTmp2 != null) {
                                        fosNewTmp2.close();
                                    }
                                    if (fTmpFile != null) {
                                        fTmpFile.delete();
                                    }
                                    throw th;
                                }
                            }
                            if (zosNewTmp != null) {
                                try {
                                    zosNewTmp.finish();
                                    zosNewTmp.close();
                                } catch (IOException e3) {
                                    if (fTmpFile != null) {
                                        try {
                                            fTmpFile.delete();
                                        } catch (Throwable th3) {
                                            th = th3;
                                            throw th;
                                        }
                                    }
                                }
                            }
                            if (fosNewTmp2 != null) {
                                fosNewTmp2.close();
                            }
                            if (fTmpFile != null) {
                                fTmpFile.delete();
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            fTmpFile = fTmpFile2;
                            fNewFile = fNewFile2;
                            if (zosNewTmp != null) {
                                zosNewTmp.finish();
                                zosNewTmp.close();
                            }
                            if (fosNewTmp2 != null) {
                                fosNewTmp2.close();
                            }
                            if (fTmpFile != null) {
                                fTmpFile.delete();
                            }
                            throw th;
                        }
                    }
                    fNewFile2.createNewFile();
                    fosNewTmp = new FileOutputStream(fNewFile2);
                } catch (Exception e4) {
                    fNewFile = fNewFile2;
                    if (fNewFile != null) {
                        fNewFile.delete();
                    }
                    if (zosNewTmp != null) {
                        zosNewTmp.finish();
                        zosNewTmp.close();
                    }
                    if (fosNewTmp2 != null) {
                        fosNewTmp2.close();
                    }
                    if (fTmpFile != null) {
                        fTmpFile.delete();
                    }
                } catch (Throwable th5) {
                    th = th5;
                    fNewFile = fNewFile2;
                    if (zosNewTmp != null) {
                        zosNewTmp.finish();
                        zosNewTmp.close();
                    }
                    if (fosNewTmp2 != null) {
                        fosNewTmp2.close();
                    }
                    if (fTmpFile != null) {
                        fTmpFile.delete();
                    }
                    throw th;
                }
                try {
                    ZipOutputStream zosNewTmp2 = new ZipOutputStream(new BufferedOutputStream(fosNewTmp));
                    if (zfTmp != null) {
                        try {
                            Enumeration<? extends ZipEntry> zipTmp = zfTmp.entries();
                            while (zipTmp.hasMoreElements()) {
                                ZipEntry zeTmp = (ZipEntry) zipTmp.nextElement();
                                zosNewTmp2.putNextEntry(zeTmp);
                                if (!zeTmp.isDirectory()) {
                                    CopyDataByStream(zfTmp.getInputStream(zeTmp), zosNewTmp2);
                                }
                                zosNewTmp2.closeEntry();
                            }
                        } catch (Exception e5) {
                            fNewFile = fNewFile2;
                            zosNewTmp = zosNewTmp2;
                            fosNewTmp2 = fosNewTmp;
                        } catch (Throwable th6) {
                            th = th6;
                            fNewFile = fNewFile2;
                            zosNewTmp = zosNewTmp2;
                            fosNewTmp2 = fosNewTmp;
                        }
                    }
                    for (File fCur : fSrcList) {
                        AddFileToZipOutStream(fCur, new StringBuilder(String.valueOf(fCur.getParentFile().getName())).append("/").append(fCur.getName()).toString(), zosNewTmp2);
                        if (bDelAfterProc) {
                            fCur.delete();
                        }
                    }
                    if (zosNewTmp2 != null) {
                        try {
                            zosNewTmp2.finish();
                            zosNewTmp2.close();
                        } catch (IOException e6) {
                        }
                    }
                    if (fosNewTmp != null) {
                        fosNewTmp.close();
                    }
                    if (fTmpFile != null) {
                        try {
                            fTmpFile.delete();
                        } catch (Throwable th7) {
                            th = th7;
                            fNewFile = fNewFile2;
                            zosNewTmp = zosNewTmp2;
                            fosNewTmp2 = fosNewTmp;
                            throw th;
                        }
                    }
                    fNewFile = fNewFile2;
                    zosNewTmp = zosNewTmp2;
                    fosNewTmp2 = fosNewTmp;
                } catch (Exception e7) {
                    fNewFile = fNewFile2;
                    fosNewTmp2 = fosNewTmp;
                    if (fNewFile != null) {
                        fNewFile.delete();
                    }
                    if (zosNewTmp != null) {
                        zosNewTmp.finish();
                        zosNewTmp.close();
                    }
                    if (fosNewTmp2 != null) {
                        fosNewTmp2.close();
                    }
                    if (fTmpFile != null) {
                        fTmpFile.delete();
                    }
                } catch (Throwable th8) {
                    th = th8;
                    fNewFile = fNewFile2;
                    fosNewTmp2 = fosNewTmp;
                    if (zosNewTmp != null) {
                        zosNewTmp.finish();
                        zosNewTmp.close();
                    }
                    if (fosNewTmp2 != null) {
                        fosNewTmp2.close();
                    }
                    if (fTmpFile != null) {
                        fTmpFile.delete();
                    }
                    throw th;
                }
            } catch (Exception e8) {
                if (fNewFile != null) {
                    fNewFile.delete();
                }
                if (zosNewTmp != null) {
                    zosNewTmp.finish();
                    zosNewTmp.close();
                }
                if (fosNewTmp2 != null) {
                    fosNewTmp2.close();
                }
                if (fTmpFile != null) {
                    fTmpFile.delete();
                }
            }
        }
    }

    public static void ZipFile(File fSrcFile, String strZipFile, boolean bDelAfterProc) {
        try {
            ZipFile(new File[]{fSrcFile}, strZipFile, bDelAfterProc);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void ZipFile(String strDirPath, String strFilter, String strZipFile, boolean bDelAfterProc) {
        File[] flst = new File(strDirPath).listFiles(new C03181(strFilter));
        try {
            if (flst != null && flst.length > 0) {
                ZipFile(flst, strZipFile, bDelAfterProc);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void ZipFile(String strDirPath, String strZipFile, boolean bDelAfterProc) {
        ZipFile(strDirPath, null, strZipFile, bDelAfterProc);
    }

    private static void AddFileToZipOutStream(File fSrcFile, String strEntryName, ZipOutputStream zosDst) throws IOException {
        byte[] bTmp = new byte[8192];
        zosDst.putNextEntry(new ZipEntry(strEntryName));
        FileInputStream fileInputStream = new FileInputStream(fSrcFile);
        while (true) {
            int iReadCount = fileInputStream.read(bTmp);
            if (iReadCount <= 0) {
                zosDst.flush();
                zosDst.closeEntry();
                fileInputStream.close();
                return;
            }
            zosDst.write(bTmp, 0, iReadCount);
        }
    }

    private static void CopyDataByStream(InputStream isSrc, OutputStream osDst) throws IOException {
        byte[] bTmp = new byte[8192];
        while (true) {
            int iReadCount = isSrc.read(bTmp);
            if (iReadCount <= 0) {
                osDst.flush();
                return;
            }
            osDst.write(bTmp, 0, iReadCount);
        }
    }
}
