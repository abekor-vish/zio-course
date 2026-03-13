package com.rockthejvm.ziocourse

import zio.*
import zio.Console.* 
import scala.io.Source
import java.io.{IOException, File}
import java.util.concurrent.TimeoutException
import zio.Cause.Fail
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl

enum FailureReason:
    case NoCommandLineArgs
    ,InvalidNumberFormat
    ,InvalidIntForGain
    ,InvalidNumberOfArgs
    ,CouldNotCreateAudioStream
    ,CouldNotCreateClip
    ,CouldNotPlayClip

import FailureReason.*

object Timer extends ZIOAppDefault {

    val DefaultGain = -30

    val usageMessage = """
        Usage: timer minutes before alarm <gain-control>
                gain-control should be something like -10 or -20
    """

    val invalidNumberMessage = "Invalid number bruhhh"
    val invalidIntGain = "Invalid Int for the Gain Peramiter"
    val invalidNumberOfArgs = "Invalid number of args"
    val couldNotCreateAudioStreamMsg = "Could Not create an audio stream"
    val couldNotCreateClipMsg = "Could not creat a clip"
    val couldNotPlayClipMsg = "Could not play the clip"


    val SoundFile = "/Users/abekor/downloads/pickupCoin.wav" // scala way of naming constant val

    def checkForZeroArgs(args: Chunk[String]): ZIO[Any, FailureReason, Unit] =
        if args.size == 0 then ZIO.fail(NoCommandLineArgs) else ZIO.succeed(())

    def checkThatWeHaveTwoArgs(args: Chunk[String]): ZIO[Any, FailureReason, Unit] = 
        if args.size == 1 || args.size == 2 then ZIO.succeed(()) else ZIO.fail(InvalidNumberOfArgs)

    def getMinutesToWait(args: Chunk[String]): ZIO[Any, FailureReason, Int] = 
        ZIO.attempt(args.head.toInt)
            .orElseFail(InvalidNumberFormat)
        
    // Chunk are lazy arrays, list, seq, used in ZIO
    def getGainControl(args: Chunk[String]): ZIO[Any, FailureReason, Int] = 
        if args.size == 2 then
            ZIO.attempt(args(1).toInt).orElseFail(InvalidIntForGain)
        else
            ZIO.succeed(DefaultGain)

    def writeSuccessMessage(minutesToWait: Int): ZIO[Any, FailureReason, Unit] =
        ZIO.succeed(println(s"timer started. wait time is $minutesToWait minutes."))
    
    def countdownEffect(minutesToWait: Int): ZIO[Any, Nothing, Unit] = 
        ZIO.foreachDiscard(1 to minutesToWait) {
            minute => ZIO.sleep(1.second) *>
                printLine(s"time remaining: ${minutesToWait - minute} ...").orDie
                // orDie handles errors in a ZIO effect by converting any failure into a defect. 
        }
    
    def playSoundFile(filePath: String, gainControl: Int): ZIO[Any, FailureReason, Unit] = 
        // read/open sound file
        // audio input stream (create)
        // Clip
        // open audio input stream
        // set the gain
        // start the clip
        // sleep
        for {
                _                   <- printLine("---- PLAYING SOUND FILE NOW ---").orDie
                file                <- ZIO.succeed(File(filePath))
                audioInputStream    <- ZIO.attempt(AudioSystem.getAudioInputStream(file)).orElseFail(CouldNotCreateAudioStream)
                clip                <- ZIO.attempt(AudioSystem.getClip).orElseFail(CouldNotCreateClip)
                _                   <- ZIO.attempt {
                                    clip.open(audioInputStream)
                                    val floatGainControl = clip.getControl(FloatControl.Type.MASTER_GAIN)
                                                            .asInstanceOf[FloatControl]
                                    floatGainControl.setValue(gainControl.toFloat)
                                    clip.start
                                }.orElseFail(CouldNotPlayClip)
                _                   <- ZIO.sleep(7.seconds)
            }
        yield ()
        

    


    val blueprint = for {
        args          <- ZIOAppArgs.getArgs
        // process command-line args
        _             <- checkForZeroArgs(args)
        _             <- checkThatWeHaveTwoArgs(args)
        minutesToWait <- getMinutesToWait(args)
        gainControl   <- getGainControl(args)
        _             <- writeSuccessMessage(minutesToWait)
        // play sound file
        _             <- countdownEffect(minutesToWait)
        _             <- playSoundFile(SoundFile, gainControl)
    } yield()

    val run = blueprint.foldZIO(
        failure => failure match {
            case NoCommandLineArgs => printLineError(usageMessage)
            case InvalidNumberOfArgs => printLineError(invalidNumberOfArgs)
            case InvalidNumberFormat => printLineError(invalidNumberMessage)
            case InvalidIntForGain => printLineError(invalidIntGain)
            case CouldNotCreateAudioStream => printLineError(couldNotCreateAudioStreamMsg)
            case CouldNotCreateClip => printLineError(couldNotCreateClipMsg)
            case CouldNotPlayClip => printLineError(couldNotPlayClipMsg)
        },
        success => ZIO.succeed(())
    )
}
