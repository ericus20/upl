module.exports = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx}",
    "./src/components/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        upsidle_white: {
          light: "#FAFAFA",
          DEFAULT: "#F2F2F2",
        },
      },

      keyframes: {
        "fade-out": {
          from: {
            opacity: "1",
            transform: "translateY(0px)",
          },
          to: {
            opacity: "0",
            transform: "translateY(5px)",
          },
        },
        "fade-in": {
          from: {
            opacity: "0",
            transform: "translateY(5px)",
          },
          to: {
            opacity: "1",
            transform: "translateY(0px)",
          },
        },
      },
      animation: {
        "fade-out": "fade-out 5s ease-out",
        "fade-in": "fade-in 5s ease-in delay-100",
      },
    },
  },
  plugins: [require("@tailwindcss/line-clamp")],
};
