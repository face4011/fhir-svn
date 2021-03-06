package org.hl7.fhir.instance.validation;

import org.hl7.fhir.instance.model.Profile.ProfileExtensionDefnComponent;
import org.hl7.fhir.instance.validation.ExtensionLocatorService.ExtensionLocationResponse;



/**
 * This interface is used to provide extension location services for the validator
 * 
 * when it encounters an extension, it asks this server to locate it, or tell it 
 * whether to ignore the extension, or mark it as invalid
 * 
 * @author Grahame
 *
 */
public interface ExtensionLocatorService {

  public enum Status {
    Located, NotAllowed, Unknown
  }

  public class ExtensionLocationResponse {
    private Status status;
    private ProfileExtensionDefnComponent definition;
		private String message;
		private String url;

    public ExtensionLocationResponse(String url, Status status, ProfileExtensionDefnComponent definition, String message) {
      super();
      this.url = url;
      this.status = status;
      this.definition = definition;
      this.message = message;
    }

    public Status getStatus() {
      return status;
    }

    public ProfileExtensionDefnComponent getDefinition() {
      return definition;
    }

		public String getMessage() {
	    return message;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    /**
     * This routine is used when walking into a complex extension. 
     * the non-tail part of the relative URL matches the end of the
     * exiting URL 
     * @param url - the relative URL
     * @return
     * @throws Exception 
     */
    public ExtensionLocationResponse clone(String url) throws Exception {
      if (!this.url.endsWith(url.substring(0, url.lastIndexOf("."))))
        throw new Exception("the relative URL "+url+" cannot be used in the context "+this.url);
      
      return new ExtensionLocationResponse(this.url+"."+url.substring(url.lastIndexOf(".")+1), status, definition, message);
    }
    
  }

  public ExtensionLocationResponse locateExtension(String uri);
}
