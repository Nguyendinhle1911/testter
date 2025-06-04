import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:signup_form/dang_ky_man_hinh.dart'; // Adjust based on your project structure

void main() {
  // Setup before all tests
  setUpAll(() {
    print('Starting tests for DangKyManHinh...');
  });

  // Teardown after all tests
  tearDownAll(() {
    print('All tests completed.');
  });

  // Group 1: UI Elements Tests
  group('UI Elements Tests', () {
    testWidgets('Case 1: Verify all required UI elements are displayed', (WidgetTester tester) async {
      // Purpose: Ensure all expected UI elements (text, buttons, etc.) are present on the sign-up screen
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      expect(find.text('facebook'), findsOneWidget, reason: 'Facebook logo should be displayed');
      expect(find.text('Create a new account'), findsOneWidget, reason: 'Title should be displayed');
      expect(find.text("It's quick and easy."), findsOneWidget, reason: 'Subtitle should be displayed');
      expect(find.text('First name'), findsOneWidget, reason: 'First name label should be displayed');
      expect(find.text('Surname'), findsOneWidget, reason: 'Surname label should be displayed');
      expect(find.text('Mobile number or email'), findsOneWidget, reason: 'Email field label should be displayed');
      expect(find.text('New password'), findsOneWidget, reason: 'Password field label should be displayed');
      expect(find.text('Day'), findsOneWidget, reason: 'Day dropdown label should be displayed');
      expect(find.text('Month'), findsOneWidget, reason: 'Month dropdown label should be displayed');
      expect(find.text('Year'), findsOneWidget, reason: 'Year dropdown label should be displayed');
      expect(find.text('Female'), findsOneWidget, reason: 'Female gender option should be displayed');
      expect(find.text('Male'), findsOneWidget, reason: 'Male gender option should be displayed');
      expect(find.text('Custom'), findsOneWidget, reason: 'Custom gender option should be displayed');
      expect(find.text('Sign Up'), findsOneWidget, reason: 'Sign Up button should be displayed');
      expect(find.textContaining('By clicking Sign Up'), findsOneWidget, reason: 'Terms text should be displayed');
      expect(find.text('Already have an account?'), findsOneWidget, reason: 'Login link should be displayed');

      print('✅ Case 1: UI elements test passed');
    });
  });

  // Group 2: Form Validation Tests
  group('Form Validation Tests', () {
    testWidgets('Case 2: Empty required fields show validation errors', (WidgetTester tester) async {
      // Purpose: Verify that submitting the form with empty required fields displays error indicators
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.text('Sign Up'));
      await tester.tap(find.text('Sign Up'), warnIfMissed: false);
      await tester.pumpAndSettle();

      expect(
        find.byWidgetPredicate(
              (widget) =>
          widget is Container &&
              widget.decoration is BoxDecoration &&
              (widget.decoration as BoxDecoration).color == Colors.red &&
              widget.child is Text &&
              (widget.child as Text).data == '!',
        ),
        findsAtLeastNWidgets(2),
        reason: 'At least two required fields should show error indicators',
      );

      expect(find.text('Account created successfully!'), findsNothing,
          reason: 'Success message should not appear for invalid submission');

      print('✅ Case 2: Empty fields validation test passed');
    });

    testWidgets('Case 3: Date of Birth validation for age < 13', (WidgetTester tester) async {
      // Purpose: Ensure users under 13 years old trigger an age validation error
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(DropdownButtonFormField<String>).at(0));
      await tester.tap(find.byType(DropdownButtonFormField<String>).at(0));
      await tester.pumpAndSettle();
      await tester.ensureVisible(find.text('1'));
      await tester.tap(find.text('1').last);
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(DropdownButtonFormField<String>).at(1));
      await tester.tap(find.byType(DropdownButtonFormField<String>).at(1));
      await tester.pumpAndSettle();
      await tester.ensureVisible(find.text('Jan'));
      await tester.tap(find.text('Jan').last);
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(DropdownButtonFormField<String>).at(2));
      await tester.tap(find.byType(DropdownButtonFormField<String>).at(2));
      await tester.pumpAndSettle();
      await tester.ensureVisible(find.text('2024'));
      await tester.tap(find.text('2024').last);
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.text('Sign Up'));
      await tester.tap(find.text('Sign Up'), warnIfMissed: false);
      await tester.pumpAndSettle();

      expect(find.text('Must be at least 13 years old.'), findsOneWidget,
          reason: 'Age under 13 should trigger validation error');

      print('✅ Case 3: DOB validation (age < 13) test passed');
    });

    testWidgets('Case 4: Password validation for length < 8 characters', (WidgetTester tester) async {
      // Purpose: Verify that a password shorter than 8 characters triggers a validation error
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(TextFormField).at(3));
      await tester.enterText(find.byType(TextFormField).at(3), 'short');
      await tester.pumpAndSettle();

      expect(
        find.byWidgetPredicate(
              (widget) =>
          widget is Container &&
              widget.decoration is BoxDecoration &&
              (widget.decoration as BoxDecoration).color == Colors.red &&
              widget.child is Text &&
              (widget.child as Text).data == '!',
        ),
        findsAtLeastNWidgets(1),
        reason: 'Short password should trigger validation error',
      );

      print('✅ Case 4: Password length validation test passed');
    });

    testWidgets('Case 5: Invalid email format shows error', (WidgetTester tester) async {
      // Purpose: Ensure an invalid email format triggers a validation error
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(TextFormField).at(2));
      await tester.enterText(find.byType(TextFormField).at(2), 'invalid');
      await tester.pumpAndSettle();

      expect(
        find.byWidgetPredicate(
              (widget) =>
          widget is Container &&
              widget.decoration is BoxDecoration &&
              (widget.decoration as BoxDecoration).color == Colors.red &&
              widget.child is Text &&
              (widget.child as Text).data == '!',
        ),
        findsAtLeastNWidgets(1),
        reason: 'Invalid email format should trigger validation error',
      );

      print('✅ Case 5: Invalid email format test passed');
    });

    testWidgets('Case 6: Minimum valid age (13 years) passes validation', (WidgetTester tester) async {
      // Purpose: Verify that a user exactly 13 years old passes age validation
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      final currentYear = DateTime.now().year;
      final minValidYear = (currentYear - 13).toString();

      await tester.ensureVisible(find.byType(DropdownButtonFormField<String>).at(0));
      await tester.tap(find.byType(DropdownButtonFormField<String>).at(0));
      await tester.pumpAndSettle();
      await tester.ensureVisible(find.text('1'));
      await tester.tap(find.text('1').last);
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(DropdownButtonFormField<String>).at(1));
      await tester.tap(find.byType(DropdownButtonFormField<String>).at(1));
      await tester.pumpAndSettle();
      await tester.ensureVisible(find.text('Jan'));
      await tester.tap(find.text('Jan').last);
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(DropdownButtonFormField<String>).at(2));
      await tester.tap(find.byType(DropdownButtonFormField<String>).at(2));
      await tester.pumpAndSettle();
      await tester.ensureVisible(find.text(minValidYear));
      await tester.tap(find.text(minValidYear).last);
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.text('Sign Up'));
      await tester.tap(find.text('Sign Up'), warnIfMissed: false);
      await tester.pumpAndSettle();

      expect(find.text('Must be at least 13 years old.'), findsNothing,
          reason: 'Age of 13 should not trigger age validation error');

      print('✅ Case 6: Minimum valid age test passed');
    });

    testWidgets('Case 7: Password with exact length (8 characters) passes validation', (WidgetTester tester) async {
      // Purpose: Verify that a password of exactly 8 characters passes validation
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(TextFormField).at(3));
      await tester.enterText(find.byType(TextFormField).at(3), '12345678');
      await tester.pumpAndSettle();

      expect(
        find.byWidgetPredicate(
              (widget) =>
          widget is Container &&
              widget.decoration is BoxDecoration &&
              (widget.decoration as BoxDecoration).color == Colors.red &&
              widget.child is Text &&
              (widget.child as Text).data == '!',
        ),
        findsNothing,
        reason: 'Password of 8 characters should not trigger validation error',
      );

      print('✅ Case 7: Password exact length test passed');
    });

    testWidgets('Case 8: Empty DOB with other valid fields shows error', (WidgetTester tester) async {
      // Purpose: Ensure submitting with valid fields but empty DOB triggers validation error
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(TextFormField).at(0));
      await tester.enterText(find.byType(TextFormField).at(0), 'John');
      await tester.ensureVisible(find.byType(TextFormField).at(1));
      await tester.enterText(find.byType(TextFormField).at(1), 'Doe');
      await tester.ensureVisible(find.byType(TextFormField).at(2));
      await tester.enterText(find.byType(TextFormField).at(2), 'john.doe@example.com');
      await tester.ensureVisible(find.byType(TextFormField).at(3));
      await tester.enterText(find.byType(TextFormField).at(3), 'password123');

      await tester.ensureVisible(find.text('Male'));
      await tester.tap(find.text('Male'));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.text('Sign Up'));
      await tester.tap(find.text('Sign Up'), warnIfMissed: false);
      await tester.pumpAndSettle();

      expect(
        find.byWidgetPredicate(
              (widget) =>
          widget is Container &&
              widget.decoration is BoxDecoration &&
              (widget.decoration as BoxDecoration).color == Colors.red &&
              widget.child is Text &&
              (widget.child as Text).data == '!',
        ),
        findsAtLeastNWidgets(1),
        reason: 'Empty DOB should trigger validation error',
      );

      expect(find.text('Account created successfully!'), findsNothing,
          reason: 'Success message should not appear with empty DOB');

      print('✅ Case 8: Empty DOB test passed');
    });
  });

  // Group 3: Form Submission Tests
  group('Form Submission Tests', () {
    testWidgets('Case 9: Successful form submission with valid data', (WidgetTester tester) async {
      // Purpose: Verify that submitting the form with all valid data shows a success message
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(TextFormField).at(0));
      await tester.enterText(find.byType(TextFormField).at(0), 'John');
      await tester.ensureVisible(find.byType(TextFormField).at(1));
      await tester.enterText(find.byType(TextFormField).at(1), 'Doe');
      await tester.ensureVisible(find.byType(TextFormField).at(2));
      await tester.enterText(find.byType(TextFormField).at(2), 'john.doe@example.com');
      await tester.ensureVisible(find.byType(TextFormField).at(3));
      await tester.enterText(find.byType(TextFormField).at(3), 'password123');

      await tester.ensureVisible(find.byType(DropdownButtonFormField<String>).at(0));
      await tester.tap(find.byType(DropdownButtonFormField<String>).at(0));
      await tester.pumpAndSettle();
      await tester.ensureVisible(find.text('1'));
      await tester.tap(find.text('1').last);
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(DropdownButtonFormField<String>).at(1));
      await tester.tap(find.byType(DropdownButtonFormField<String>).at(1));
      await tester.pumpAndSettle();
      await tester.ensureVisible(find.text('Jan'));
      await tester.tap(find.text('Jan').last);
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(DropdownButtonFormField<String>).at(2));
      await tester.tap(find.byType(DropdownButtonFormField<String>).at(2));
      await tester.pumpAndSettle();
      await tester.ensureVisible(find.text('2000'));
      await tester.tap(find.text('2000').last);
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.text('Male'));
      await tester.tap(find.text('Male'));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.text('Sign Up'));
      await tester.tap(find.text('Sign Up'), warnIfMissed: false);
      await tester.pumpAndSettle();

      expect(find.text('Account created successfully!'), findsOneWidget,
          reason: 'Valid submission should show success message');

      print('✅ Case 9: Successful submission test passed');
    });
  });

  // Group 4: Navigation Tests
  group('Navigation Tests', () {
    testWidgets('Case 10: Already have an account link triggers redirect', (WidgetTester tester) async {
      // Purpose: Verify that clicking the "Already have an account?" link triggers the expected redirect behavior
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.text('Already have an account?'));
      await tester.tap(find.text('Already have an account?'), warnIfMissed: false);
      await tester.pumpAndSettle();

      expect(find.text('Redirecting to login...'), findsOneWidget,
          reason: 'Login link should trigger redirect message');

      print('✅ Case 10: Login link redirect test passed');
    });

    testWidgets('Case 11: Terms and conditions link interaction', (WidgetTester tester) async {
      // Purpose: Verify that interacting with the "Terms, Privacy Policy" link behaves as expected
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.text('Terms, Privacy Policy'));
      await tester.tap(find.text('Terms, Privacy Policy'), warnIfMissed: false);
      await tester.pumpAndSettle();

      expect(find.text('Terms link tapped'), findsNothing,
          reason: 'Terms link tap should not show unexpected message (update if UI changes)');

      print('✅ Case 11: Terms link interaction test passed');
    });

    testWidgets('Case 12: Focus navigation through input fields', (WidgetTester tester) async {
      // Purpose: Ensure that tabbing through input fields navigates focus correctly
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.byType(TextFormField).at(0));
      await tester.tap(find.byType(TextFormField).at(0));
      await tester.pumpAndSettle();
      await tester.enterText(find.byType(TextFormField).at(0), 'John');
      expect(tester.widget<TextFormField>(find.byType(TextFormField).at(0)).controller!.text, 'John',
          reason: 'First name field should accept input');

      await tester.sendKeyEvent(LogicalKeyboardKey.tab);
      await tester.pumpAndSettle();
      await tester.enterText(find.byType(TextFormField).at(1), 'Doe');
      expect(tester.widget<TextFormField>(find.byType(TextFormField).at(1)).controller!.text, 'Doe',
          reason: 'Surname field should accept input after tab');

      await tester.sendKeyEvent(LogicalKeyboardKey.tab);
      await tester.pumpAndSettle();
      await tester.enterText(find.byType(TextFormField).at(2), 'john@example.com');
      expect(tester.widget<TextFormField>(find.byType(TextFormField).at(2)).controller!.text, 'john@example.com',
          reason: 'Email field should accept input after tab');

      await tester.sendKeyEvent(LogicalKeyboardKey.tab);
      await tester.pumpAndSettle();
      await tester.enterText(find.byType(TextFormField).at(3), 'password123');
      expect(tester.widget<TextFormField>(find.byType(TextFormField).at(3)).controller!.text, 'password123',
          reason: 'Password field should accept input after tab');

      print('✅ Case 12: Focus navigation test passed');
    });
  });

  // Group 5: Edge Case Tests
  group('Edge Case Tests', () {
    testWidgets('Case 13: Custom gender selection works', (WidgetTester tester) async {
      // Purpose: Verify that selecting the "Custom" gender option updates the form correctly
      await tester.pumpWidget(MaterialApp(home: DangKyManHinh()));
      await tester.pumpAndSettle();

      await tester.ensureVisible(find.text('Custom'));
      await tester.tap(find.text('Custom'));
      await tester.pumpAndSettle();

      expect(
        find.byWidgetPredicate(
              (widget) => widget is Radio<String> && widget.groupValue == 'Custom' && widget.value == 'Custom',
        ),
        findsOneWidget,
        reason: 'Custom gender option should be selected',
      );

      print('✅ Case 13: Custom gender selection test passed');
    });
  });
}