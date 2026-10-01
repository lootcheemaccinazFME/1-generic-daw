use iced::{
	Element, padding,
	widget::{container, text},
};

#[derive(Clone, Copy, Debug)]
pub struct Icon {
	glyph: char,
	size: f32,
}

impl Icon {
	pub const fn size(mut self, size: f32) -> Self {
		self.size = size;
		self
	}

	pub const fn glyph(self) -> char {
		self.glyph
	}
}

impl<'a, Message: 'a> From<Icon> for Element<'a, Message> {
	fn from(value: Icon) -> Self {
		container(
			text(value.glyph)
				.shaping(text::Shaping::Basic)
				.size(value.size)
				.line_height(1.0),
		)
		.padding(padding::top(0.045 * value.size).bottom(-0.045 * value.size))
		.center(value.size)
		.into()
	}
}

macro_rules! icons {
	($($name:ident = $glyph:literal),* $(,)?) => {
		$(
			pub const fn $name() -> Icon {
				Icon {
					glyph: $glyph,
					size: crate::widget::LINE_HEIGHT,
				}
			}
		)*
	};
}

icons! {
	chevron_down = '⌄',
	chevron_right = '›',
	chevron_up = '⌃',
	copy = '⧉',
	cpu = '◉',
	file = '▤',
	gavel = '⚒',
	grip_horizontal = '⠿',
	grip_vertical = '⠿',
	menu = '☰',
	mic = '♩',
	pause = 'Ⅱ',
	play = '▶',
	plus = '+',
	power = '⏻',
	rotate_ccw = '↶',
	save = '▣',
	sliders_vertical = '☷',
	snowflake = '❄',
	square = '□',
	triangle_alert = '⚠',
	volume_2 = '◖',
	x = '×',
	move_vertical = '↕',
	arrow_big_right = '➜',
	power_off = '⏼',
	folder_open = '▰',
	hourglass = '⌛',
	magnet = '∩',
	file_headphone = '♫',
	file_play = '▷',
	file_video_camera = '▣',
	circle_ellipsis = '⋯',
	arrow_up_down = '↕',
	replace = '⇄',
	panel_bottom_dashed = '▱',
	chart_no_axes_gantt = '▤',
	folder_sync = '⟳',
	file_music = '♫',
	keyboard_music = '♫',
	between_horizontal_start = '⇤',
	between_vertical_start = '⇥',
	chevrons_left_right_ellipsis = '↔',
	metronome = '♩',
	square_arrow_right_enter = '↵',
	midi_port = '♬',
}
