package org.hl7.fhir.definitions.generators.specification;

import java.util.List;

import org.hl7.fhir.definitions.model.BindingSpecification;
import org.hl7.fhir.definitions.model.Definitions;
import org.hl7.fhir.definitions.model.ElementDefn;
import org.hl7.fhir.definitions.model.ExtensionDefn;
import org.hl7.fhir.definitions.model.Invariant;
import org.hl7.fhir.definitions.model.ProfileDefn;
import org.hl7.fhir.definitions.model.ResourceDefn;
import org.hl7.fhir.definitions.model.TypeRef;
import org.hl7.fhir.definitions.model.BindingSpecification.Binding;
import org.hl7.fhir.definitions.model.BindingSpecification.BindingStrength;
import org.hl7.fhir.tools.publisher.PageProcessor;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.HeirarchicalTableGenerator.Cell;
import org.hl7.fhir.utilities.xhtml.HeirarchicalTableGenerator.Piece;
import org.hl7.fhir.utilities.xhtml.HeirarchicalTableGenerator.Row;
import org.hl7.fhir.utilities.xhtml.HeirarchicalTableGenerator.TableModel;
import org.hl7.fhir.utilities.xhtml.HeirarchicalTableGenerator;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

public class ProfileTableGenerator extends TableGenerator {
  public ProfileTableGenerator(String dest, PageProcessor page) {
    super(dest, page);
  }

  public XhtmlNode generate(ProfileDefn p) throws Exception {
    HeirarchicalTableGenerator gen = new HeirarchicalTableGenerator(dest);
    TableModel model = gen.new TableModel();
    
    model.getTitles().add(gen.new Title(null, null, "Name", null, null, 0));
    model.getTitles().add(gen.new Title(null, null, "Card.", null, null, 0));
    model.getTitles().add(gen.new Title(null, null, "Type", null, null, 100));
    model.getTitles().add(gen.new Title(null, null, "Description", null, null, 0));
    model.getTitles().add(gen.new Title(null, null, "Constraints", null, null, 0));
    
    genProfile(gen, model.getRows(), p);
    
    return gen.generate(model);
  }

  private void genProfile(HeirarchicalTableGenerator gen, List<Row> rows, ProfileDefn p) throws Exception {
    Row r = gen.new Row();
    rows.add(r);
    r.setIcon("icon_profile.png");
    r.getCells().add(gen.new Cell(null, null, p.metadata("name"), null, null));
    r.getCells().add(gen.new Cell());
    r.getCells().add(gen.new Cell(null, null, "Profile", null, null));
    r.getCells().add(gen.new Cell(null, null, p.metadata("description"), null, null));
    r.getCells().add(gen.new Cell());

    if (!p.getExtensions().isEmpty()) {
      Row re = gen.new Row();
      r.getSubRows().add(re);
      re.setIcon("icon_extension.png");
      re.getCells().add(gen.new Cell(null, null, "Extensions", null, null));
      re.getCells().add(gen.new Cell());
      re.getCells().add(gen.new Cell());
      re.getCells().add(gen.new Cell(null, null, "Extensions defined by this profile", null, null));
      re.getCells().add(gen.new Cell(null, null, "Namespace: "+p.metadata("extension.uri"), null, null));
      for (ExtensionDefn ext : p.getExtensions()) {
        genExtension(gen, re.getSubRows(), ext);
      }
    }
    
    for (ResourceDefn res : p.getResources()) {
      genResourceProfile(gen, r.getSubRows(), res);
    }
  }

  private void genResourceProfile(HeirarchicalTableGenerator gen, List<Row> rows, ResourceDefn res) throws Exception {
    Row r = gen.new Row();
    rows.add(r);
    r.setIcon("icon_resource.png");
    r.getCells().add(gen.new Cell(null, null, res.getRoot().getProfileName(), null, null));
    r.getCells().add(gen.new Cell());
    r.getCells().add(gen.new Cell(null, null, res.getRoot().getName(), null, null));
    r.getCells().add(gen.new Cell(null, null, res.getDefinition(), null, null));
    r.getCells().add(gen.new Cell());
    
    for (ElementDefn c : res.getRoot().getElements())
      r.getSubRows().add(genElement(c, gen, false));
  }

  private void genExtension(HeirarchicalTableGenerator gen, List<Row> rows, ExtensionDefn ext) {
    Row r = gen.new Row();
    rows.add(r);
    r.getCells().add(gen.new Cell(null, null, ext.getCode(), null, null));
    r.getCells().add(gen.new Cell(null, null, ext.getDefinition().describeCardinality(), null, null));
    r.getCells().add(gen.new Cell(null, null, ext.getDefinition().typeCode(), null, null));
    r.getCells().add(gen.new Cell(null, null, ext.getDefinition().getShortDefn(), null, null));
    r.getCells().add(gen.new Cell(null, null, describeExtensionContext(ext), null, null));
    
  }

  private String describeExtensionContext(ExtensionDefn ext) {
    switch (ext.getType()) {
    case DataType: return "Use on data type: "+ext.getContext();
    case Elements: return "Use on element: "+ext.getContext();
    case Extension: return "Use on extension: "+ext.getContext();
    case Resource: return "Use on resource: "+ext.getContext();
    case Mapping: return "Use where element has mapping: "+ext.getContext();
    }
    return null;
  }
}