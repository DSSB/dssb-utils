package dssb.utils.pipeable.stream;

import static dssb.utils.pipeable.Operators.to;
import static dssb.utils.pipeable.stream.StreamOperations.collect;
import static dssb.utils.pipeable.stream.StreamOperations.spread;
import static dssb.utils.pipeable.stream.StreamOperations.map;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import dssb.utils.pipeable.IPipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

public class StreamPipeableTest {
    
    @Data
    @Accessors(chain=true)
    static class Company implements IPipe<Company> {
        List<Department> departments = new ArrayList<>();
    }
    
    @Data
    @Accessors(chain=true)
    static class Department implements IPipe<Department> {
        List<Person> members = new ArrayList<>();
    }
    
    @Data
    @Accessors(chain=true)
    @AllArgsConstructor
    static class Person implements IPipe<Person> {
        private String name;
    }
    
    @Test
    public void testStreamPipe() {
        val company = new Company();
        val dep1     = new Department();
        val dep2     = new Department();
        company.getDepartments().addAll(asList(dep1, dep2));
        
        dep1.getMembers().addAll(asList(
                new Person("Alice"),
                new Person("Bob"),
                new Person("Chalie")));
        
        dep2.getMembers().addAll(asList(
                new Person("Donald"),
                new Person("Edward"),
                new Person("Frank")));
        
        val r = company.pipe(
                to     (Company   ::getDepartments),
                spread(Department::getMembers),
                map    (Person    ::getName),
                collect(Collectors.toList()),
                to     (Object    ::toString)
        );
        assertEquals("[Alice, Bob, Chalie, Donald, Edward, Frank]", r);
    }
    
}
