import  platform
import os

from collections import defaultdict
from dataclasses import dataclass, asdict
from typing import List, Dict, Tuple
from tree_sitter import Parser
from tree_sitter import Language
import tree_sitter_java

import os,sys
os.chdir(sys.path[0])

system = platform.system()
if system == 'Windows':
    lib_ext = '.dll'
else:
    lib_ext = '.so'

lib_path = os.path.join('build', f'my-languages{lib_ext}')
if not os.path.exists(lib_path):
    Language.build_library(
        lib_path,
        ['lib/tree-sitter-java']
    )
JAVA_LANGUAGE = Language(lib_path, 'java')
# JAVA_LANGUAGE = Language(tree_sitter_java.language())



@dataclass
class Method:
    method_name: str
    signature: str
    line_range: List[int]
    class_path: str


class JavaTreeSitterParser:
    def __init__(self, file):
        if file:
            with open(file, 'r', encoding='utf-8') as f:
                code_str = f.read()
            self.code = (bytes(code_str, "utf8"))
            self.lines = code_str.splitlines()
            self.parser = Parser()
            self.parser.set_language(JAVA_LANGUAGE)
            self.tree = self.parser.parse(self.code)
            self.root = self.tree.root_node
            
    def parse_code_str(self, code_str):
        self.code = (bytes(code_str, "utf8"))
        self.lines = code_str.splitlines()
        self.parser = Parser()
        self.parser.set_language(JAVA_LANGUAGE)
        self.tree = self.parser.parse(self.code)
        self.root = self.tree.root_node
        return self.root

    def get_methods(self) -> List[Method]:
        result = []
        class_stack = []

        def node_text(node):
            return self.code[node.start_byte:node.end_byte].decode("utf8")

        def walk(node):
            if node.type in {
                "class_declaration",
                "enum_declaration"
            }:
                name_node = node.child_by_field_name("name")
                if name_node:
                    class_stack.append(node_text(name_node))

            elif node.type in { "method_declaration", "constructor_declaration"}:
                has_body = False
                for child in node.children:
                    if child.type == "block":
                        has_body = True
                if has_body:
                    method_name_node = node.child_by_field_name("name")
                    parameters_node = node.child_by_field_name("parameters")
                    return_type_node = node.child_by_field_name("type")

                    # 提取 modifiers（多个修饰词）
                    modifiers_node = node.child_by_field_name("modifiers")
                    modifiers = ""
                    if modifiers_node:
                        modifiers_parts = [
                            node_text(child)
                            for child in modifiers_node.children
                            if child.type != ","
                        ]
                        modifiers = " ".join(modifiers_parts)

                    method_name = node_text(method_name_node) if method_name_node else ""
                    return_type = node_text(return_type_node).strip() if return_type_node else ""

                    # 提取参数类型
                    param_types = []
                    if parameters_node:
                        for param in parameters_node.children:
                            if param.type == "formal_parameter":
                                type_node = param.child_by_field_name("type")
                                if type_node:
                                    param_types.append(node_text(type_node).strip())

                    parameter_str = ",".join(param_types)
                    signature = f"{return_type} {method_name}({parameter_str})"

                    start_line = node.start_point[0] + 1
                    end_line = node.end_point[0] + 1
                    class_path = ".".join(class_stack)

                    result.append(
                        Method(
                            method_name=method_name,
                            signature=signature,
                            line_range=[start_line, end_line],
                            class_path=class_path
                        )
                    )

            for child in node.children:
                walk(child)

            if node.type in {
                "class_declaration",
                "enum_declaration"
            }:
                class_stack.pop()

        walk(self.root)
        return result
    
    def get_nodes_count(self) -> int:
        """返回语法树中的所有节点总数"""
        count = 0

        def walk(node):
            nonlocal count
            count += 1
            for child in node.children:
                walk(child)

        walk(self.root)
        return count

    
    def get_tokens_count(self) -> int:
        """返回语法树中所有叶子节点（token）的数量"""
        count = 0

        def walk(node):
            nonlocal count
            if len(node.children) == 0:
                count += 1
            else:
                for child in node.children:
                    walk(child)

        walk(self.root)
        return count

    def get_method_signature(self):
        methods = self.get_methods()
        normalized_signatures = [method.signature for method in methods]

        return normalized_signatures