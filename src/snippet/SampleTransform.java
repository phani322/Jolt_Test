package snippet;


import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.bazaarvoice.jolt.SpecDriven;
import com.bazaarvoice.jolt.Transform;
import com.bazaarvoice.jolt.traversr.SimpleTraversal;
import com.google.common.collect.ImmutableMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SampleTransform implements SpecDriven, Transform {

	private final Object spec;
	private ImmutableMap<SimpleTraversal<Object>, Chainr> inceptionMap;

	@Inject
	public SampleTransform( Object spec ) {
		this.spec = spec;

		Map<String, String> inputSpec = (Map<String, String>) spec;

		ImmutableMap.Builder<SimpleTraversal<Object>, Chainr> builder = ImmutableMap.builder();

		for ( Map.Entry<String,String> entry : inputSpec.entrySet() ) {
			String path = entry.getKey();
			String classPathToTransform = entry.getValue();

			// SimpleTraversal is a tool for walking a Tree of Json data.
			// It is based on what Shiftr uses to write data into the output.
			SimpleTraversal<Object> traversal = new SimpleTraversal<>( path );

			// Look up the Chainr Spec from the ClassPath, and instantiate a Chainr
			List<Object> chainrSpecJson = JsonUtils.classpathToList( classPathToTransform );
			Chainr chainr = Chainr.fromSpec( chainrSpecJson );

			builder.put( traversal, chainr );
		}

		inceptionMap = builder.build();

	}

	@Override
	public Object transform( Object input ) {

		System.out.println("input : " + input);
		
		try {
			LinkedHashMap obj = (LinkedHashMap)input;//parser.parse(input.toString());
			
			System.out.println(" HERE 1 :" + obj.get("errorCode"));
			
			obj.put("ERRORTEXT", "Not found");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return input;
	}
}

