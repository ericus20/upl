import { StarIcon as StarOutlined } from "@heroicons/react/outline";
import { StarIcon } from "@heroicons/react/solid";

import { FaStarHalfAlt } from "react-icons/fa";

interface RatingStarProps {
  rate: number;
  count: number;
}

const RatingStar: React.FC<RatingStarProps> = ({ rate, count }) => {
  const includeHalf = !Number.isInteger(rate);
  const flooredRate = Math.floor(rate);
  const starDifference = 5 - flooredRate;

  return (
    <div className="flex space-x-1 items-center">
      {Array.from({ length: flooredRate }, (_, i) =>
        includeHalf && i === flooredRate - 1 ? (
          <FaStarHalfAlt key={i} className="h-5 text-yellow-500" />
        ) : (
          <StarIcon key={i} className="h-5 text-yellow-500" />
        )
      )}

      {Array.from({ length: starDifference }, (_, i) => (
        <StarOutlined key={i + flooredRate} className="h-5 text-yellow-500" />
      ))}
      <span>({count})</span>
    </div>
  );
};

export default RatingStar;
