
package com.gzwanhong.webserviceClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.gzwanhong.webserviceClient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddRecord_QNAME = new QName("http://webservice.gzwanhong.com", "addRecord");
    private final static QName _AddRecordResponse_QNAME = new QName("http://webservice.gzwanhong.com", "addRecordResponse");
    private final static QName _DownloadFile_QNAME = new QName("http://webservice.gzwanhong.com", "downloadFile");
    private final static QName _DownloadFileResponse_QNAME = new QName("http://webservice.gzwanhong.com", "downloadFileResponse");
    private final static QName _GetAllFile_QNAME = new QName("http://webservice.gzwanhong.com", "getAllFile");
    private final static QName _GetAllFileResponse_QNAME = new QName("http://webservice.gzwanhong.com", "getAllFileResponse");
    private final static QName _GetBackupFile_QNAME = new QName("http://webservice.gzwanhong.com", "getBackupFile");
    private final static QName _GetBackupFileResponse_QNAME = new QName("http://webservice.gzwanhong.com", "getBackupFileResponse");
    private final static QName _StartCheckFile_QNAME = new QName("http://webservice.gzwanhong.com", "startCheckFile");
    private final static QName _StartCheckFileResponse_QNAME = new QName("http://webservice.gzwanhong.com", "startCheckFileResponse");
    private final static QName _TestMethod_QNAME = new QName("http://webservice.gzwanhong.com", "testMethod");
    private final static QName _TestMethodResponse_QNAME = new QName("http://webservice.gzwanhong.com", "testMethodResponse");
    private final static QName _Exception_QNAME = new QName("http://webservice.gzwanhong.com", "Exception");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.gzwanhong.webserviceClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddRecord }
     * 
     */
    public AddRecord createAddRecord() {
        return new AddRecord();
    }

    /**
     * Create an instance of {@link AddRecordResponse }
     * 
     */
    public AddRecordResponse createAddRecordResponse() {
        return new AddRecordResponse();
    }

    /**
     * Create an instance of {@link DownloadFile }
     * 
     */
    public DownloadFile createDownloadFile() {
        return new DownloadFile();
    }

    /**
     * Create an instance of {@link DownloadFileResponse }
     * 
     */
    public DownloadFileResponse createDownloadFileResponse() {
        return new DownloadFileResponse();
    }

    /**
     * Create an instance of {@link GetAllFile }
     * 
     */
    public GetAllFile createGetAllFile() {
        return new GetAllFile();
    }

    /**
     * Create an instance of {@link GetAllFileResponse }
     * 
     */
    public GetAllFileResponse createGetAllFileResponse() {
        return new GetAllFileResponse();
    }

    /**
     * Create an instance of {@link GetBackupFile }
     * 
     */
    public GetBackupFile createGetBackupFile() {
        return new GetBackupFile();
    }

    /**
     * Create an instance of {@link GetBackupFileResponse }
     * 
     */
    public GetBackupFileResponse createGetBackupFileResponse() {
        return new GetBackupFileResponse();
    }

    /**
     * Create an instance of {@link StartCheckFile }
     * 
     */
    public StartCheckFile createStartCheckFile() {
        return new StartCheckFile();
    }

    /**
     * Create an instance of {@link StartCheckFileResponse }
     * 
     */
    public StartCheckFileResponse createStartCheckFileResponse() {
        return new StartCheckFileResponse();
    }

    /**
     * Create an instance of {@link TestMethod }
     * 
     */
    public TestMethod createTestMethod() {
        return new TestMethod();
    }

    /**
     * Create an instance of {@link TestMethodResponse }
     * 
     */
    public TestMethodResponse createTestMethodResponse() {
        return new TestMethodResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "addRecord")
    public JAXBElement<AddRecord> createAddRecord(AddRecord value) {
        return new JAXBElement<AddRecord>(_AddRecord_QNAME, AddRecord.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRecordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "addRecordResponse")
    public JAXBElement<AddRecordResponse> createAddRecordResponse(AddRecordResponse value) {
        return new JAXBElement<AddRecordResponse>(_AddRecordResponse_QNAME, AddRecordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "downloadFile")
    public JAXBElement<DownloadFile> createDownloadFile(DownloadFile value) {
        return new JAXBElement<DownloadFile>(_DownloadFile_QNAME, DownloadFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "downloadFileResponse")
    public JAXBElement<DownloadFileResponse> createDownloadFileResponse(DownloadFileResponse value) {
        return new JAXBElement<DownloadFileResponse>(_DownloadFileResponse_QNAME, DownloadFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "getAllFile")
    public JAXBElement<GetAllFile> createGetAllFile(GetAllFile value) {
        return new JAXBElement<GetAllFile>(_GetAllFile_QNAME, GetAllFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "getAllFileResponse")
    public JAXBElement<GetAllFileResponse> createGetAllFileResponse(GetAllFileResponse value) {
        return new JAXBElement<GetAllFileResponse>(_GetAllFileResponse_QNAME, GetAllFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBackupFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "getBackupFile")
    public JAXBElement<GetBackupFile> createGetBackupFile(GetBackupFile value) {
        return new JAXBElement<GetBackupFile>(_GetBackupFile_QNAME, GetBackupFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBackupFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "getBackupFileResponse")
    public JAXBElement<GetBackupFileResponse> createGetBackupFileResponse(GetBackupFileResponse value) {
        return new JAXBElement<GetBackupFileResponse>(_GetBackupFileResponse_QNAME, GetBackupFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartCheckFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "startCheckFile")
    public JAXBElement<StartCheckFile> createStartCheckFile(StartCheckFile value) {
        return new JAXBElement<StartCheckFile>(_StartCheckFile_QNAME, StartCheckFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartCheckFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "startCheckFileResponse")
    public JAXBElement<StartCheckFileResponse> createStartCheckFileResponse(StartCheckFileResponse value) {
        return new JAXBElement<StartCheckFileResponse>(_StartCheckFileResponse_QNAME, StartCheckFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestMethod }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "testMethod")
    public JAXBElement<TestMethod> createTestMethod(TestMethod value) {
        return new JAXBElement<TestMethod>(_TestMethod_QNAME, TestMethod.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestMethodResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "testMethodResponse")
    public JAXBElement<TestMethodResponse> createTestMethodResponse(TestMethodResponse value) {
        return new JAXBElement<TestMethodResponse>(_TestMethodResponse_QNAME, TestMethodResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gzwanhong.com", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

}
