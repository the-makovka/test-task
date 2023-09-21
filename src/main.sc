require: slotfilling/slotFilling.sc
  module = sys.zb-common

theme: /
    state: Правила
      intent: /Давай поиграем
      a: Игра "Быки и коровы". Я загадаю 4-значное число, и ты должен попробовать отгадать его. Число не содержит повторяющихся цифр. Готов начать?
      go!: /Правила/Согласен?
      
      state: Согласен?

        state: Да
            intent: /Согласие
            go!: /Игра

        state: Нет
            intent: /Несогласие
            a: Ну и ладно! Если передумаешь — скажи "давай поиграем"
    
    state: Игра
      script:
        $session.secretNumber = generateSecretNumber()
        $session.attempts = 0
        $reactions.say("Отлично! Я загадал число. Попробуй угадать.")
    
    state: Проверка
      intent: /Число
      script:
        val guess = $parseTree.number.joinToString("")
        if (guess.length != 4 || !guess.all { it.isDigit() }) {
          $reactions.say("Пожалуйста, введите 4-значное число.")
          return
        }
    
        $session.attempts++
        val secretNumber = $session.secretNumber
        val bulls = countBulls(secretNumber, guess)
        val cows = countCows(secretNumber, guess)
    
        if (bulls == 4) {
          $reactions.say("Поздравляю, ты угадал число $secretNumber! Ты сделал $session.attempts попыток. Хочешь сыграть ещё?")
          $reactions.go("/Правила/Давай поиграем")
        } else {
          $reactions.say("Ты угадал $bulls бык(а) и $cows коров(у).")
        }
    
    state: NoMatch || noContext = true
      event: noMatch
      random:
        a: Я не понял. Попробуй ввести 4-значное число.
        a: Я не могу понять, попробуй ввести число ещё раз.
