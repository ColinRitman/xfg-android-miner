#!/usr/bin/env python3
"""
Create Fuego-themed PNG images for the Android app
"""

from PIL import Image, ImageDraw, ImageFont
import os

def create_fuego_icon(size, filename, is_foreground=False):
    """Create a Fuego-themed icon with the given size"""
    # Create image with transparent background
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # Fuego colors - orange/red theme
    fuego_orange = (255, 140, 0)  # Orange
    fuego_red = (220, 20, 60)     # Crimson
    fuego_gold = (255, 215, 0)    # Gold
    fuego_dark = (139, 69, 19)    # Saddle brown
    
    # Draw a stylized flame/coin design
    center = size // 2
    
    if is_foreground:
        # For adaptive icon foreground - simpler design
        # Draw a stylized "F" for Fuego
        font_size = size // 2
        try:
            # Try to use a bold font
            font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", font_size)
        except:
            # Fallback to default font
            font = ImageFont.load_default()
        
        # Draw "F" in the center
        text = "F"
        bbox = draw.textbbox((0, 0), text, font=font)
        text_width = bbox[2] - bbox[0]
        text_height = bbox[3] - bbox[1]
        text_x = (size - text_width) // 2
        text_y = (size - text_height) // 2
        
        draw.text((text_x, text_y), text, fill=fuego_orange, font=font)
        
        # Add a small flame accent
        flame_points = [
            (center - size//6, center + size//4),
            (center, center - size//4),
            (center + size//6, center + size//4),
            (center, center + size//6)
        ]
        draw.polygon(flame_points, fill=fuego_red)
    else:
        # For regular icons - more detailed design
        # Draw outer circle (coin shape)
        margin = size // 8
        draw.ellipse([margin, margin, size-margin, size-margin], 
                    fill=fuego_gold, outline=fuego_dark, width=2)
        
        # Draw inner flame design
        inner_size = size // 2
        inner_margin = (size - inner_size) // 2
        
        # Main flame body
        flame_points = [
            (center, inner_margin),
            (center - inner_size//3, inner_margin + inner_size//2),
            (center - inner_size//6, inner_margin + inner_size//3),
            (center + inner_size//6, inner_margin + inner_size//3),
            (center + inner_size//3, inner_margin + inner_size//2),
            (center, inner_margin + inner_size//2)
        ]
        draw.polygon(flame_points, fill=fuego_orange)
        
        # Add "XFG" text
        try:
            font_size = size // 6
            font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", font_size)
        except:
            font = ImageFont.load_default()
        
        text = "XFG"
        bbox = draw.textbbox((0, 0), text, font=font)
        text_width = bbox[2] - bbox[0]
        text_height = bbox[3] - bbox[1]
        text_x = (size - text_width) // 2
        text_y = center + inner_size//4
        
        draw.text((text_x, text_y), text, fill=fuego_dark, font=font)
    
    # Save the image
    img.save(filename, 'PNG')
    print(f"Created {filename} ({size}x{size})")

def main():
    """Create all required Fuego images"""
    # Create directory if it doesn't exist
    os.makedirs('temp_images', exist_ok=True)
    
    # Define the sizes for different densities
    sizes = {
        'mdpi': 48,
        'hdpi': 72,
        'xhdpi': 96,
        'xxhdpi': 144,
        'xxxhdpi': 192
    }
    
    # Create fuegold.png for each density
    for density, size in sizes.items():
        filename = f'temp_images/fuegold_{density}.png'
        create_fuego_icon(size, filename, is_foreground=False)
    
    # Create fuegold_foreground.png for adaptive icon
    create_fuego_icon(108, 'temp_images/fuegold_foreground.png', is_foreground=True)
    
    print("\nAll images created successfully!")

if __name__ == "__main__":
    main()