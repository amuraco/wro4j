package ro.isdc.wro.model.resource.processor.support;

import static ro.isdc.wro.util.WroTestUtils.initProcessor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;


/**
 * A processor which uses a list of processors to compute the result.
 *
 * @author Alex Objelean
 * @since 1.6.4
 * @created 17 Apr 2013
 */
public class ChainedProcessor
    implements ResourcePreProcessor {
  private List<ResourcePreProcessor> processors = new ArrayList<ResourcePreProcessor>();

  private ChainedProcessor(final List<ResourcePreProcessor> processors) {
    this.processors = processors;
  }

  public static ChainedProcessor create(final ResourcePreProcessor... processors) {
    final List<ResourcePreProcessor> processorsAsList = new ArrayList<ResourcePreProcessor>();
    if (processors != null) {
      processorsAsList.addAll(Arrays.asList(processors));
    }
    return new ChainedProcessor(processorsAsList);
  }

  /**
   * {@inheritDoc}
   */
  public void process(final Resource resource, final Reader reader, final Writer writer)
      throws IOException {
    Reader tempReader = reader;
    Writer tempWriter = new StringWriter();
    for (final ResourcePreProcessor processor : processors) {
      tempWriter = new StringWriter();
      initProcessor(processor);
      processor.process(resource, tempReader, tempWriter);
      tempReader = new StringReader(tempWriter.toString());
    }
    writer.write(tempWriter.toString());
  }
}