/* eslint-disable @next/next/no-img-element */
import { Carousel } from "react-responsive-carousel";
import "react-responsive-carousel/lib/styles/carousel.min.css"; // requires a loader

const Banner = () => {
  return (
    <div className="relative">
      {/* Add gradient layout to the carousel from botton to top */}
      <div className="absolute w-full h-80 rounded-sm bg-gradient-to-t from-gray-100 to to-transparent bottom-0 z-20" />

      <Carousel
        autoPlay
        infiniteLoop
        showStatus={false}
        showIndicators={false}
        showThumbs={false}
        interval={5000}
      >
        <div>
          <img
            alt=""
            src="https://image.shutterstock.com/image-vector/big-sale-cashback-discount-offer-600w-705625729.jpg"
            loading="lazy"
          />
        </div>
        <div>
          <img
            alt=""
            src="https://image.shutterstock.com/image-vector/ad-banner-natural-beauty-products-600w-1780339220.jpg"
            loading="lazy"
          />
        </div>
        <div>
          <img
            alt=""
            src="https://image.shutterstock.com/image-vector/ad-banner-simple-beauty-products-600w-1780339235.jpg"
            loading="lazy"
          />
        </div>
      </Carousel>
    </div>
  );
};

export default Banner;
