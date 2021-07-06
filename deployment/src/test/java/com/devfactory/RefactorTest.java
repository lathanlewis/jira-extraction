package com.devfactory;

import static org.assertj.core.api.Assertions.assertThat;

import com.devfactory.cdk.base.Main;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.cxapi.CloudAssembly;

/**
 * Snapshot tests test the synthesized AWS CloudFormation template against a previously-stored baseline template.
 * This way, when you're refactoring your app, you can be sure that the refactored code works exactly the same way
 * as the original. If the changes were intentional, you can accept a new baseline for future tests.
 * Ref: https://docs.aws.amazon.com/cdk/latest/guide/testing.html
 */
public class RefactorTest {

    private static final File BASELINE_FILE = new File("build/baseline.json");
    private static final ObjectMapper JSON =
        new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Disabled("Enable this just before you start refactoring")
    @Test
    public void compareToBaseline() throws IOException {
        TreeNode actual = cloudFormationTemplate();
        TreeNode expected;
        try {
            expected = loadBaseline();
        } catch (IOException e) {
            saveBaseline(actual);
            // free pass until next run
            expected = actual;
        }
        assertThat(expected).isEqualTo(actual);
    }

    public void saveBaseline(TreeNode cfData) throws IOException {
        JSON.writer().createGenerator(BASELINE_FILE, JsonEncoding.UTF8).writeTree(cfData);
    }

    public TreeNode loadBaseline() throws IOException {
        return JSON.reader().createParser(BASELINE_FILE).readValueAsTree();
    }

    public JsonNode cloudFormationTemplate() {
        Main main = new Main();
        main.run();
        CloudAssembly cf = main.getCloudAssembly();

        ObjectNode tree = JSON.createObjectNode();
        cf.getStacks().stream().forEach(stack -> {
            JsonNode stackJson = JSON.valueToTree(stack.getTemplate());
            tree.set(stack.getStackName(), stackJson);
        });
        return tree;
    }

}
