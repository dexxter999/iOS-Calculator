package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.example.calculator.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private val errorMessage = "Error!"
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonLayout.forEach { view ->
            val button = view as MaterialButton
            button.setOnClickListener {
                if (button.id in listOf(
                    binding.buttonAC.id,
                    binding.buttonEqual.id,
                    binding.buttonPlusMinus.id
                )) {
                    return@setOnClickListener
                }
                if (binding.resultTextView.text.isBlank() && button.id == binding.buttonDot.id) {
                    return@setOnClickListener
                }
                if (binding.resultTextView.text.toString() == errorMessage) {
                    binding.resultTextView.text = ""
                }
                binding.resultTextView.append(button.text)

            }
        }

        binding.buttonAC.setOnClickListener {
            binding.resultTextView.text = ""
        }

        binding.buttonEqual.setOnClickListener {

            binding.resultTextView.text = try {
                  evaluateExpression(binding.resultTextView.text.toString()).toString()
            } catch (e:Exception) {
                 errorMessage
            }
        }

        binding.buttonPlusMinus.setOnClickListener {
            val expression = binding.resultTextView.text.toString()
            binding.resultTextView.text = inverseLast(expression)
        }


    }

    private fun inverseLast(expression: String): String {
        if (expression.all { it.isDigit() || it == '.' }) {
            return "-$expression"
        }
        return buildString {
            var isReversed = false
            expression.reversed().forEach { char ->
                if (char.isDigit() || isReversed || char == '.') {
                    append(char)
                } else {
                    when (char) {
                        '-' -> append('+')
                        '+' -> append('-')
                        else -> {
                            append('-')
                            append(char)
                        }
                    }
                    isReversed = true
                }
            }
        }.reversed()
    }




    private fun evaluateExpression(expression: String): Double {
        return ExpressionBuilder(expression)
            .build()
            .evaluate()
    }


}

