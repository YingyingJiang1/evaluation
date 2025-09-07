
    
def create_single_file_task():
    return """
# Task
You are given a Java code snippet that may contain **inconsistent coding styles** .
Your task is to determine the dominant style of the input code, and then refactor the code snippet to ensure it is stylistically consistent with that dominant style. You must preserve all original functionality while making the file stylistically coherent.
Output your transformed code and a brief explanation of your transformations in given format.

"""
    
    

def create_prompt(task, style_aspects, src_code, style_examples=""):
    strategy = """
# Strategy
- **Identify the dominant style** used in the examples. This includes but is not limited to formatting, naming conventions, comment style, and structure preferences.
- **Align all inconsistent parts** of the file with the dominant style.

"""
    
    style_descriptions = f"""
# Style Aspects to Consider
The stylistic alignment includes but is not limited to:
{style_aspects}

"""

    constraints = """
# Constraints
- The refactored code must be **semantically equivalent** to the original.
- You must **not change any behavior** of the input code snippet or its interaction with external code (e.g., public method signatures, public API behavior, class names, or package structure).
- Only make changes that affect **code style**, not logic or execution.

"""

    input = f"""

# Input

{src_code}

{style_examples}
"""

    output = """
# Output

```java
<your transformed code here>
```

```text
/**
<your explanation here>
*/
```
"""

    return f"{task}{strategy}{style_descriptions}{constraints}{input}{output}"


def create_src_code(filepath):
    with open(filepath) as f:
        content = f.read()
        
    return f"## Source Code\n\n```java\n{content}\n```\n"


def create_general_style_aspects():
    return "- Formatting and layout\n- Naming conventions\n- Commenting\n- Structure preference\n- The arrangement order of semantically irrelevant elements"

def create_detail_style_aspects():
    return """
- Space: Specifies whether spaces are present between adjacent tokens.
- Indentation: Specifies the unit and type of space used for indentation.
- Line Statement: Specifies whether one statement or multiple statements appear on a single line.
- Blank Line: Specifies the ratio of blank lines to program lines.
- Line Length: Specifies the maximum length of a single line.
- Brace & Compound Statement Body Placement: Specifies the newline placement around curly braces and unbraced body of compound statements.
- Naming Format: Specifies the case format and word-splitting style for user-defined identifiers, such as class, method, and variable names. Common formats include underscore and camel case.
- Comment Density: Refers to the ratio of comment lines to program lines.
- Operator Preferences: Preferences for alternative operator usage: (a$>$b, b$<$a), (a$<=$b, b$>=$a), (a==False, !a), (a==True, a).
- Modifiers Order: Specifies the preferred order of modifiers (e.g., public, static, final).
- Ordering of Class Contents: Specifies the preferred ordering of contents within a class.
- Syntax of Control Structures: Specifies the syntax and structure of control structures(e.g:switch,loops).
- Optional Brace: Whether to use braces for single-line blocks.
- Number of Declarations: Refers to the preferred number of variable declarations in a statement.
- Literal Usage: Specifies whether hard-coded literals should be used (excluding -1, 0, and 1) or constants should be declared.
- Assignment Statement Preferences: Specifies preferences regarding the formatting and structure of assignment statements.
- Increment or Decrement Preferences: Refers to the preferred style for increment and decrement operations.
- Continuous Logic and Conditions Preferences: Specifies preferences for continuous logic and conditions.
- Continuous Logic or Conditions Preferences: Specifies preferences for continuous logical or conditional expressions.
- Multi-branch: Refers to preferences related to multi-branch control structures.
- Continuous Assignments or Calls Preferences: Specifies preferences for continuous assignments with the same value or for method calls on the same object.
- Array Declaration Style: Specifies the preferred style for declaring arrays.
- Return Statements: Specifies the style for return statements.
- Check then Return Preferences: Specifies preferences for check-then-return structures.
- Check then Assign Preferences: Specifies preferences for check-then-assign structures.
- Continue Preferences: Refers to preferences regarding the use of the `continue` statement.
- Redundant Code: Specifies the preferred approach for eliminating or minimizing redundant code.
- Loops: Refers to the preferred structure and formatting of loop constructs.
- Literal Position in Boolean Expression: Specifies the preferred position of literals in boolean expressions.
- Maximum Expression Complexity: Specifies the maximum length and depth of all expressions.
- Maximum Function Complexity: Specifies the maximum code lines and nesting depth of all methods.
- Code Logic: Specifies the order of if-else blocks.
- Location of Local Variable Declaration: Specifies where local variables are declared. The two common styles are at the block start and near their use.

"""

def create_single_file_prompt(filepath, style_ascpects="general"):
    style_ascpects = create_general_style_aspects() if style_ascpects == "general" else create_detail_style_aspects()

    return create_prompt(create_single_file_task(), 
                         style_ascpects, create_src_code(filepath))
    
    
def create_task_speicific_prompt(inconsistencies, file_path):
    task = "You are given a code snippet where some parts are written in a different style than the rest. \
        Your goal is to rewrite the inconsistent parts so that the entire code \
            follows a uniform style, while preserving the original functionality."
    input = f"""

# Input

{create_src_code(file_path)}

## Inconsistent parts
{inconsistencies}

"""

    output = """
# Output

```java
<your transformed code here>
```

```text
/**
<your explanation here>
*/
```
"""

    constraints = """
# Constraints
- The refactored code must be **semantically equivalent** to the original.
- You must **not change any behavior** of the input code snippet or its interaction with external code (e.g., method signatures, public API behavior, class names, or package structure).
- Only make changes that affect **code style**, not logic or execution.

"""
    return f"{task}\n{input}{output}{constraints}"