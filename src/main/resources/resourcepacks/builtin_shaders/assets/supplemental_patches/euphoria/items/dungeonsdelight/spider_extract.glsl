if (color.r > color.g * 2 && color.r > color.b * 2) {
    emission = 1.5;
    color.gb = pow1_5(color.gb);
}