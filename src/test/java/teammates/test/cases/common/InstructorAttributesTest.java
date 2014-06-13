package teammates.test.cases.common;

import static org.testng.AssertJUnit.*;
import static teammates.common.util.Const.EOL;
import static teammates.common.util.FieldValidator.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import teammates.common.datatransfer.InstructorAttributes;
import teammates.common.datatransfer.InstructorPrivileges;
import teammates.common.util.Const;
import teammates.common.util.StringHelper;
import teammates.storage.entity.Instructor;
import teammates.test.cases.BaseTestCase;

public class InstructorAttributesTest extends BaseTestCase {

    @BeforeClass
    public static void setupClass() throws Exception {
        printTestClassHeader();
    }

    @Test
    public void testValidate() {
        
        InstructorAttributes i = new InstructorAttributes("valid.google.id", "valid-course-id", "valid name", "valid@email.com");
        
        assertEquals(true, i.isValid());
        
        i.googleId = "invalid@google@id";
        i.name = "";
        i.email = "invalid email";
        i.courseId = "";
        
        assertEquals("invalid value", false, i.isValid());
        String errorMessage = 
                String.format(GOOGLE_ID_ERROR_MESSAGE, i.googleId, REASON_INCORRECT_FORMAT) + EOL 
                + String.format(COURSE_ID_ERROR_MESSAGE, i.courseId, REASON_EMPTY) + EOL 
                + String.format(PERSON_NAME_ERROR_MESSAGE, i.name, REASON_EMPTY)+ EOL
                + String.format(EMAIL_ERROR_MESSAGE, i.email, REASON_INCORRECT_FORMAT);  
        assertEquals("invalid value", errorMessage, StringHelper.toString(i.getInvalidityInfo()));
    }
    
    @Test
    public void testConstructor() {
        InstructorAttributes instructor = new InstructorAttributes("valid.google.id", "valid-course-id", "valid name", "valid@email.com");
        String roleName = Const.InstructorPermissionRoleNames.INSTRUCTOR_PERMISSION_ROLE_COOWNER;
        String displayedName = Const.InstructorPermissionRoleNames.INSTRUCTOR_PERMISSION_ROLE_COOWNER;
        InstructorPrivileges privileges = new InstructorPrivileges(Const.InstructorPermissionRoleNames.INSTRUCTOR_PERMISSION_ROLE_COOWNER);
        
        assertEquals(roleName, instructor.role);
        assertEquals(displayedName, instructor.displayedName);
        assertEquals(privileges, instructor.privileges);
        
        InstructorAttributes instructor1 = new InstructorAttributes(instructor.googleId, instructor.courseId, instructor.name, instructor.email,
                instructor.role, instructor.displayedName, instructor.instructorPrivilegesAsText);
        
        assertEquals(privileges, instructor1.privileges);
        
        InstructorAttributes instructor2 = new InstructorAttributes(instructor.googleId, instructor.courseId, instructor.name, instructor.email,
                instructor.role, instructor.displayedName, instructor1.privileges);
        
        assertEquals(instructor1.privileges, instructor2.privileges);
        
        Instructor entity = instructor2.toEntity();
        InstructorAttributes instructor3 = new InstructorAttributes(entity);
        
        assertEquals(instructor2.googleId, instructor3.googleId);
        assertEquals(instructor2.courseId, instructor3.courseId);
        assertEquals(instructor2.name, instructor3.name);
        assertEquals(instructor2.email, instructor3.email);
        assertEquals(instructor2.role, instructor3.role);
        assertEquals(instructor2.displayedName, instructor3.displayedName);
        assertEquals(instructor2.privileges, instructor3.privileges);
        
        entity.setRole(null);
        entity.setDisplayedName(null);
        entity.setInstructorPrivilegeAsText(null);
        InstructorAttributes instructor4 = new InstructorAttributes(entity);
        
        assertEquals(instructor2.googleId, instructor3.googleId);
        assertEquals(instructor2.courseId, instructor3.courseId);
        assertEquals(instructor2.name, instructor3.name);
        assertEquals(instructor2.email, instructor3.email);
        // default values for these
        assertEquals(instructor2.role, instructor3.role);
        assertEquals(instructor2.displayedName, instructor3.displayedName);
        assertEquals(instructor2.privileges, instructor3.privileges);
    }
    
    @Test
    public void testIsRegistered() {
        InstructorAttributes instructor = new InstructorAttributes("valid.google.id", "valid-course-id", "valid name", "valid@email.com");       
        assertTrue(instructor.isRegistered());
        
        instructor.googleId = null;
        assertFalse(instructor.isRegistered());     
    }
    
    @Test
    public void testToEntity() {
        String googleId = "valid.googleId";
        String courseId = "courseId";
        String name = "name";
        String email = "email@google.com";
        String roleName = Const.InstructorPermissionRoleNames.INSTRUCTOR_PERMISSION_ROLE_COOWNER;
        String displayedName = Const.InstructorPermissionRoleNames.INSTRUCTOR_PERMISSION_ROLE_COOWNER;
        InstructorPrivileges privileges = new InstructorPrivileges(Const.InstructorPermissionRoleNames.INSTRUCTOR_PERMISSION_ROLE_COOWNER);
        InstructorAttributes instructor = new InstructorAttributes(googleId, courseId, name, email, roleName, displayedName, privileges);
        String key = "randomKey";
        instructor.key = key;
        
        Instructor entity = instructor.toEntity();
        assertEquals(key, entity.getRegistrationKey());
    }
    
    @AfterClass
    public static void tearDown() {
        printTestClassFooter();
    }

}
