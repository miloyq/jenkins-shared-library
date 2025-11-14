package io.github.miloyq.jsl.log

enum Color {
    RESET('\u001B[0m'),
    RED('\u001B[31m'),
    GREEN('\u001B[32m'),
    YELLOW('\u001B[33m'),
    CYAN('\u001B[36m')

    final String code

    Color(String code) {
        this.code = code
    }
}