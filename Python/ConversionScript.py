import os
import subprocess


def convert_ogg_to_wav(input_dir, output_dir):
    """
    Converts all .ogg files in the input directory to .wav files in the output directory.

    Args:
        input_dir (str): Path to the directory containing .ogg files.
        output_dir (str): Path to the directory to save the .wav files.
    """
    # Ensure the output directory exists
    os.makedirs(output_dir, exist_ok=True)

    # Iterate through all files in the input directory
    count = 1
    for file_name in os.listdir(input_dir):
        if file_name.endswith(".ogg"):
            ogg_file_path = os.path.join(input_dir, file_name)
            wav_file_name = f"_0{count}.wav"
            wav_file_path = os.path.join(output_dir, wav_file_name)
            count += 1

            try:
                # Run the FFmpeg command
                subprocess.run(
                    ["ffmpeg", "-i", ogg_file_path, wav_file_path],
                    check=True,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.PIPE
                )
                print(f"Converted: {ogg_file_path} -> {wav_file_path}")
            except subprocess.CalledProcessError as e:
                print(f"Failed to convert {ogg_file_path}: {e.stderr.decode()}")


if __name__ == "__main__":
    # Paths to input and output directories
    input_directory = "/home/talha/Desktop/Game Assets/UI and Music stuff"  # Replace with your input directory
    output_directory = "/home/talha/Desktop/Game Assets/UI and Music stuff/Music_Wav_Num"  # Replace with your output directory

    convert_ogg_to_wav(input_directory, output_directory)
