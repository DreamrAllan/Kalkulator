package com.example.kalkulator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kalkulator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Initialize ViewBinding
    private lateinit var binding: ActivityMainBinding

    // Variables to store current input, result, operator, and state
    private var inputString = ""
    private var hasil = 0.0
    private var operasiSekarang = ""
    private var operasiAktif = false
    private var lastActionWasEqual = false  // New variable to track if the last action was equal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize binding with LayoutInflater
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup button click listeners using binding
        setupNumberButtons()
        setupOperatorButtons()

        // Setup click listeners for equals, clear, and backspace buttons
        binding.samadengan.setOnClickListener { onEqualClicked() }
        binding.c.setOnClickListener { onClearClicked() }
        binding.imgArrow.setOnClickListener { onBackspaceClicked() }  // Using ImageView as backspace
    }

    private fun setupNumberButtons() {
        // List of number buttons
        val numberButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )

        // Set click listener for each number button
        for (button in numberButtons) {
            button.setOnClickListener {
                onButtonClicked(button.text.toString())
            }
        }
    }

    private fun setupOperatorButtons() {
        // Set click listener for each operator button
        binding.tambah.setOnClickListener { onButtonClicked("+") }
        binding.minus.setOnClickListener { onButtonClicked("-") }
        binding.kali.setOnClickListener { onButtonClicked("*") }
        binding.bagi.setOnClickListener { onButtonClicked("/") }
    }

    private fun onButtonClicked(input: String) {
        if (lastActionWasEqual && input !in listOf("+", "-", "*", "/")) {
            // If last action was equals and a number is pressed, clear the input string
            inputString = ""
            binding.hasil.text = "" // Clear the displayed result
        }
        lastActionWasEqual = false  // Reset the flag

        if (input in listOf("+", "-", "*", "/")) {
            if (inputString.isNotEmpty() && !operasiAktif) {
                operasiSekarang = input
                inputString += " $operasiSekarang "
                operasiAktif = true
            }
        } else {
            inputString += input
            operasiAktif = false
        }
        binding.hitungan.text = inputString
    }

    private fun calculate() {
        try {
            val tokens = inputString.split(" ")
            var result = tokens[0].toDouble()
            var operator = ""

            for (i in 1 until tokens.size) {
                val token = tokens[i]

                when {
                    token in listOf("+", "-", "*", "/") -> operator = token
                    token.isNotEmpty() -> {
                        val number = token.toDouble()
                        result = when (operator) {
                            "+" -> result + number
                            "-" -> result - number
                            "*" -> result * number
                            "/" -> result / number
                            else -> result
                        }
                    }
                }
            }

            hasil = result

            // Format the result to display without .0 for integers
            binding.hasil.text = if (hasil == hasil.toInt().toDouble()) {
                hasil.toInt().toString() // Convert to integer and then to string if it's a whole number
            } else {
                hasil.toString() // Show as a double if there's a decimal
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error in calculation", Toast.LENGTH_SHORT).show()
        }
    }



    private fun onEqualClicked() {
        if (inputString.isNotEmpty()) {
            calculate()
            binding.hitungan.text = hasil.toString()
            inputString = hasil.toString()
            operasiAktif = false
            lastActionWasEqual = true  // Set the flag to true after showing result
        }
    }

    private fun onClearClicked() {
        inputString = ""
        hasil = 0.0
        operasiSekarang = ""
        operasiAktif = false
        lastActionWasEqual = false  // Reset the flag when cleared
        binding.hitungan.text = "0"
        binding.hasil.text = "0"
        Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show()
    }

    private fun onBackspaceClicked() {
        if (inputString.isNotEmpty()) {
            // Remove the last character or space from the input string
            inputString = inputString.trimEnd()  // Trim any spaces at the end
            inputString = if (inputString.last() == ' ') {
                inputString.dropLast(3)  // Remove the operator and the spaces around it
            } else {
                inputString.dropLast(1)  // Remove the last character
            }
            binding.hitungan.text = inputString.ifEmpty { "0" }
        }
    }
}
