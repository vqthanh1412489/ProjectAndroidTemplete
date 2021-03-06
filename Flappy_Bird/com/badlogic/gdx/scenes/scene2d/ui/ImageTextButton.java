package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.esotericsoftware.tablelayout.Cell;

public class ImageTextButton extends Button {
    private final Image image;
    private final Label label;
    private ImageTextButtonStyle style;

    public static class ImageTextButtonStyle extends TextButtonStyle {
        public Drawable imageChecked;
        public Drawable imageCheckedOver;
        public Drawable imageDisabled;
        public Drawable imageDown;
        public Drawable imageOver;
        public Drawable imageUp;

        public ImageTextButtonStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
            super(up, down, checked, font);
        }

        public ImageTextButtonStyle(ImageTextButtonStyle style) {
            super(style);
            if (style.imageUp != null) {
                this.imageUp = style.imageUp;
            }
            if (style.imageDown != null) {
                this.imageDown = style.imageDown;
            }
            if (style.imageOver != null) {
                this.imageOver = style.imageOver;
            }
            if (style.imageChecked != null) {
                this.imageChecked = style.imageChecked;
            }
            if (style.imageCheckedOver != null) {
                this.imageCheckedOver = style.imageCheckedOver;
            }
            if (style.imageDisabled != null) {
                this.imageDisabled = style.imageDisabled;
            }
        }

        public ImageTextButtonStyle(TextButtonStyle style) {
            super(style);
        }
    }

    public ImageTextButton(String text, Skin skin) {
        this(text, (ImageTextButtonStyle) skin.get(ImageTextButtonStyle.class));
        setSkin(skin);
    }

    public ImageTextButton(String text, Skin skin, String styleName) {
        this(text, (ImageTextButtonStyle) skin.get(styleName, ImageTextButtonStyle.class));
        setSkin(skin);
    }

    public ImageTextButton(String text, ImageTextButtonStyle style) {
        super((ButtonStyle) style);
        this.style = style;
        defaults().space(3.0f);
        this.image = new Image();
        this.image.setScaling(Scaling.fit);
        add(this.image);
        this.label = new Label((CharSequence) text, new LabelStyle(style.font, style.fontColor));
        this.label.setAlignment(1);
        add(this.label);
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
    }

    public void setStyle(ButtonStyle style) {
        if (style instanceof ImageTextButtonStyle) {
            super.setStyle(style);
            this.style = (ImageTextButtonStyle) style;
            if (this.image != null) {
                updateImage();
            }
            if (this.label != null) {
                ImageTextButtonStyle textButtonStyle = (ImageTextButtonStyle) style;
                LabelStyle labelStyle = this.label.getStyle();
                labelStyle.font = textButtonStyle.font;
                labelStyle.fontColor = textButtonStyle.fontColor;
                this.label.setStyle(labelStyle);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("style must be a ImageTextButtonStyle.");
    }

    public ImageTextButtonStyle getStyle() {
        return this.style;
    }

    private void updateImage() {
        boolean isPressed = isPressed();
        if (this.isDisabled && this.style.imageDisabled != null) {
            this.image.setDrawable(this.style.imageDisabled);
        } else if (isPressed && this.style.imageDown != null) {
            this.image.setDrawable(this.style.imageDown);
        } else if (this.isChecked && this.style.imageChecked != null) {
            Image image = this.image;
            Drawable drawable = (this.style.imageCheckedOver == null || !isOver()) ? this.style.imageChecked : this.style.imageCheckedOver;
            image.setDrawable(drawable);
        } else if (isOver() && this.style.imageOver != null) {
            this.image.setDrawable(this.style.imageOver);
        } else if (this.style.imageUp != null) {
            this.image.setDrawable(this.style.imageUp);
        }
    }

    public void draw(SpriteBatch batch, float parentAlpha) {
        Color fontColor;
        updateImage();
        if (this.isDisabled && this.style.disabledFontColor != null) {
            fontColor = this.style.disabledFontColor;
        } else if (isPressed() && this.style.downFontColor != null) {
            fontColor = this.style.downFontColor;
        } else if (this.isChecked && this.style.checkedFontColor != null) {
            fontColor = (!isOver() || this.style.checkedOverFontColor == null) ? this.style.checkedFontColor : this.style.checkedOverFontColor;
        } else if (!isOver() || this.style.overFontColor == null) {
            fontColor = this.style.fontColor;
        } else {
            fontColor = this.style.overFontColor;
        }
        if (fontColor != null) {
            this.label.getStyle().fontColor = fontColor;
        }
        super.draw(batch, parentAlpha);
    }

    public Image getImage() {
        return this.image;
    }

    public Cell getImageCell() {
        return getCell(this.image);
    }

    public Label getLabel() {
        return this.label;
    }

    public Cell getLabelCell() {
        return getCell(this.label);
    }

    public void setText(String text) {
        this.label.setText(text);
    }

    public CharSequence getText() {
        return this.label.getText();
    }
}
