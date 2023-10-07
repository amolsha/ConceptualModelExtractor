

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

/** A demo illustrating how to call the OpenIE system programmatically.
 */
public class CME implements Serializable{
	TreeMap<String,ArrayList<Relationship>> sourceRelationshipMap = null;
	TreeMap<String,Integer> sourceRelationshipSetSizeMap = null;
	
	ArrayList<Relationship> relationshipList = null;
	Relationship relationship = null;

	TreeMap<String, ArrayList<TagInfo>> sourceTagsMap = null;
	ArrayList<TagInfo> tagsList = null;
	TagInfo tagInfo = null;

	boolean isContainsAll(String s1, String s2){
		String[] split = s2.split(" ");

		for(int i=0; i<split.length; i++){
			if(!s1.contains(split[i])){
				return false;
			}
		}
		return true;
	}

	ArrayList<Relationship> getFinalRelationshipSet(ArrayList<Relationship> relationships)
	{
		if(relationships.size()>=1)
		{
			ArrayList<Relationship> relationshipsCopy = new ArrayList<Relationship>();
			relationshipsCopy.addAll(relationships);
			ArrayList<Relationship> relationshipsTemp = new ArrayList<Relationship>();
			//HashSet<Relationship> relationshipsSet = new HashSet<Relationship>();
			Relationship relTemp = relationships.get(0);
			Collections.sort(relationships,new ObjectComparator());
			for(Relationship reln1: relationships)
			{
				for(Relationship reln2: relationships)
				{
					if(new Test().isContainsAll(reln1.getSubject()+" "+reln1.getRelation()+" "+reln1.getObject(), reln2.getSubject()+" "+reln2.getRelation()+" "+reln2.getObject()) && ((reln1.getSubject()+" "+reln1.getRelation()+" "+reln1.getObject()).length()>(reln2.getSubject()+" "+reln2.getRelation()+" "+reln2.getObject()).length()))
					{
						relTemp=reln1;
						relationshipsTemp.add(reln2);
					}
				}
				//relationshipsSet.add(relTemp);
			}
			relationshipsCopy.removeAll(relationshipsTemp);
			return relationshipsCopy;
		}
		else
			return null;
	}

	public void extractRelationships(String datasetFileName) throws Exception {
		// Create the Stanford CoreNLP pipeline
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,pos,lemma,ner,depparse,coref,natlog,openie");
		props.setProperty("openie.resolve_coref", "true");
//		props.setProperty("openie.format", "ollie");
//		props.setProperty("openie.affinity_probability_cap","0.33");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		List<String> userStories = Files.readAllLines(Paths.get(datasetFileName));
		sourceRelationshipMap = new TreeMap<String,ArrayList<Relationship>>();
		sourceRelationshipSetSizeMap = new TreeMap<String,Integer>();
		sourceTagsMap = new TreeMap<String,ArrayList<TagInfo>>();

		for (String userStory: userStories) {
			userStory = userStory.trim();
			System.out.println(userStory);
			tagsList = new ArrayList<TagInfo>();
			CoreDocument document = pipeline.processToCoreDocument(userStory);
			// display tokens
			for (CoreLabel tok : document.tokens()) {
				//				System.out.println(tok.ner());
				String tagName = null;
				try
				{
					tagName=new TagNames().tagNamesMap.get(tok.tag());
				}
				catch(Exception e)
				{
					tagName = "";
				}

				tagInfo = new TagInfo(tok.word(),tok.tag(),tagName,tok.lemma());
				tagsList.add(tagInfo);
				if (userStory.equals("As a librarian, I want to search books according to their title, author, subject category, and publication date or place reservations"))
					System.out.println(tagInfo);
				//System.out.println(String.format("%s\t%s", tok.word(), tok.tag()));
			}
			sourceTagsMap.put(userStory,tagsList);

			// Annotate an example document.
			Annotation doc = new Annotation(userStory);
			pipeline.annotate(doc);
			int originalRelSetSize = 0;
			// Loop over sentences in the document
			for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
				// Get the OpenIE triples for the sentence
				//System.out.println(sentence);

				Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
				// Print the triples
				String subject = null;
				String object = null;
				String replacedSubject = null;
				
				relationshipList = new ArrayList<Relationship>();
				originalRelSetSize = triples.size();
				for (RelationTriple triple : triples) {
					System.out.println(triple.subjectGloss() + "->"+triple.relationGloss()+"->"+triple.objectGloss()+" (Confidence: "+triple.confidenceGloss()+")");
					if (triple.relationGloss().toLowerCase().equals("want as")){
						subject = triple.objectGloss();
					}
				}

				for (RelationTriple triple : triples) {
					if (!triple.relationGloss().toLowerCase().equals("want") && !triple.relationGloss().toLowerCase().equals("want as"))
					{
						object = replacePrimaryActor(triple.objectGloss(),subject);
						replacedSubject = replacePrimaryActor(triple.subjectGloss(), subject);
						relationship = new Relationship(replacedSubject,object,triple.relationGloss(),triple.confidence);
						relationshipList.add(relationship);
//						if (triple.subjectGloss().toLowerCase().equals("i"))
//						{
//							relationship = new Relationship(subject,object,triple.relationGloss(),triple.confidence);
//							relationshipList.add(relationship);
//						}
//						else
//						{
//							relationship = new Relationship(triple.subjectGloss(),object,triple.relationGloss(),triple.confidence);
//							relationshipList.add(relationship);
//						}
					}
				}	
				Collections.sort(relationshipList,new RelationComparator());
				for(Relationship relationship:relationshipList)
				{
					System.out.println(relationship);
				}
				sourceRelationshipMap.put(String.valueOf(sentence),relationshipList);
				sourceRelationshipSetSizeMap.put(String.valueOf(sentence), originalRelSetSize);
			}
			//			System.out.println(sourceRelationshipMap);
		}
	}

	String replacePrimaryActor(String object,String primaryActor)
	{
		String[] splittedObject = object.split("\\s");
		for(int i=0;i<splittedObject.length;i++)
		{
			if(splittedObject[i].length()==1 && splittedObject[i].toLowerCase().equals("i"))
			{
				splittedObject[i]=primaryActor;
			}
		}
		String replacedObject = String.join(" ", splittedObject);
		return replacedObject;
	}
}

