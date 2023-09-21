require: slotfilling/slotFilling.sc
  module = sys.zb-common
require: common.js
    module = sys.zb-common
    
theme: /

    state: Правила
      intent: /Давай поиграем
      a: Игра "Быки и коровы". Я загадаю 4-значное число, и ты должен попробовать угадать его. Число не содержит повторяющихся цифр. Готов начать?
      go: /Игра
    
    state: Игра
      script:
        var secretNumber = generateSecretNumber()
        session.set("secretNumber", secretNumber)
        reactions.say("Отлично! Я загадал число. Попробуй угадать его.")
        reactions.go("/Проверка")
    
    state: Проверка
      intent: /Число
      script:
        var guess = parseTree._Number.joinToString("")
        
        if (guess.length != 4 || !guess.all { it.isDigit() }) {
          reactions.say("Пожалуйста, введите 4-значное число.")
          reactions.go("/Проверка")
        } else {
          var secretNumber = session.get("secretNumber") as String
          var bulls = countBulls(secretNumber, guess)
          var cows = countCows(secretNumber, guess)
    
          reactions.say("В числе $guess у тебя $bulls бык(а) и $cows коров(у). Попробуй ещё раз!")
        }
    
    state: NoMatch || noContext = true
      event: noMatch
      random:
        a: Я не понял. Попробуй ввести 4-значное число.
        a: Я не могу понять, попробуй ввести число ещё раз.
