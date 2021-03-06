package org.hl7.fhir.definitions.generators.specification;

import java.io.File;

import org.hl7.fhir.definitions.model.BindingSpecification;
import org.hl7.fhir.definitions.model.Definitions;
import org.hl7.fhir.definitions.model.ElementDefn;
import org.hl7.fhir.definitions.model.BindingSpecification.Binding;
import org.hl7.fhir.instance.model.AtomEntry;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.tools.publisher.PageProcessor;
import org.hl7.fhir.utilities.Utilities;

public class BaseGenerator {
  protected PageProcessor page;
  protected Definitions definitions;

  protected String getBindingLink(ElementDefn e) throws Exception {
    BindingSpecification bs = definitions.getBindingByName(e.getBindingName());
    if (bs.getBinding() == Binding.Reference)
      return bs.getReference();      
    else if (bs.getBinding() == Binding.CodeList)
      return bs.getReference().substring(1)+".html";
    else if (bs.getBinding() == Binding.ValueSet) {
      if (Utilities.noString(bs.getReference())) 
        return "??";
      else if (bs.getReference().startsWith("valueset-"))
        return bs.getReference()+".html";            
      else if (bs.getReference().startsWith("http://hl7.org/fhir")) {
        if (bs.getReference().startsWith("http://hl7.org/fhir/v3/vs/")) {
          AtomEntry<ValueSet> vs = page.getValueSets().get(bs.getReference());
          return vs.getLinks().get("path").replace(File.separatorChar, '/');
        } else if (bs.getReference().startsWith("http://hl7.org/fhir/v2/vs/")) {
            AtomEntry<ValueSet> vs = page.getValueSets().get(bs.getReference());
            return vs.getLinks().get("path").replace(File.separatorChar, '/');
        } else if (bs.getReference().startsWith("http://hl7.org/fhir/vs/"))
          return bs.getReference().substring(23)+".html";
        else
          throw new Exception("Internal reference "+bs.getReference()+" not handled yet");
      } else
        return bs.getReference()+".html";            
    } else if (bs.getBinding() == Binding.Special) {
      if (bs.getName().equals("MessageEvent"))
        return "message-events.html";
      else if (bs.getName().equals("ResourceType"))
        return "resource-types.html";
      else if (bs.getName().equals("DataType"))
        return "data-types.html";
      else if (bs.getName().equals("FHIRDefinedType"))
        return "defined-types.html";
      else 
        throw new Exception("Unknown special type "+bs.getName());
    } else 
      throw new Exception("not handled yet");
  }


}
