-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Mar 04, 2020 at 08:22 AM
-- Server version: 5.6.44-cll-lve
-- PHP Version: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `foodrecipe_app`
--
CREATE DATABASE IF NOT EXISTS `foodrecipe_app` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `foodrecipe_app`;

-- --------------------------------------------------------

--
-- Table structure for table `tb_fr_category`
--

CREATE TABLE `tb_fr_category` (
  `id` int(11) NOT NULL,
  `img_url` varchar(200) NOT NULL,
  `country_name` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_fr_category`
--

INSERT INTO `tb_fr_category` (`id`, `img_url`, `country_name`) VALUES
(1, 'indian.jpg', 'Indian'),
(2, 'japanese.jpg', 'Japanese'),
(3, 'chinese.jpg', 'Chinese'),
(4, 'american.jpg', 'American'),
(5, 'french.jgp', 'French'),
(6, 'malaysian.jpg', 'Malaysian'),
(7, 'mexican.jpg', 'Mexican'),
(8, 'thai.jpg', 'Thai');

-- --------------------------------------------------------

--
-- Table structure for table `tb_fr_rating`
--

CREATE TABLE `tb_fr_rating` (
  `rating` float NOT NULL,
  `uname` varchar(50) NOT NULL,
  `recipe_name` varchar(50) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_fr_rating`
--

INSERT INTO `tb_fr_rating` (`rating`, `uname`, `recipe_name`) VALUES
(4, 'rishabh', 'Beef Carrot Stew'),
(4.5, 'rishabh', 'Gluten Free Onion Rings'),
(4.5, 'rishabh', 'Rhubarb Cake with Meringue'),
(5, 'rishabh', 'Keto Peppermint Hot Cocoa'),
(4.5, 'rishabh', 'Curry '),
(5, 'rishabh', ' Vegetable Pulava'),
(2.5, 'rishabh', 'Rajma'),
(5, 'rishabh', 'Cake'),
(4.5, 'rishabh', 'Biryani'),
(4.5, 'rishabh', 'Chicken 65 Burger'),
(3, 'rishabh', 'Steak with lemon and capers'),
(5, 'rishabh', 'BBQ Chicken'),
(5, 'rishabh', 'Protein Balls'),
(5, 'rishabh', 'French Vanilla Blackberry Pancakes'),
(4, 'rishabh', 'Cod with Tomato-Olive-Chorizo Sauce and Mashed Pot'),
(4, 'rishabh', 'Turkey and Rice Stuffed Acorn Squash'),
(3.5, 'parwinder', 'Curry '),
(4.5, 'parwinder', ' Vegetable Pulava'),
(4, 'parwinder', 'Rajma'),
(3, 'parwinder', 'Cake'),
(4.5, 'parwinder', 'Biryani'),
(4, 'parwinder', 'Chicken 65 Burger'),
(4, 'parwinder', 'Protein Balls'),
(4.5, 'parwinder', 'BBQ Chicken'),
(3.5, 'parwinder', 'French Vanilla Blackberry Pancakes'),
(4.5, 'parwinder', 'Turkey and Rice Stuffed Acorn Squash'),
(5, 'parwinder', 'Cod with Tomato-Olive-Chorizo Sauce and Mashed Pot'),
(3, 'parwinder', 'BBQ Chicken'),
(3.5, 'parwinder', 'BBQ Chicken'),
(4.5, 'parwinder', 'BBQ Chicken'),
(4.5, 'shilpa', 'Curry '),
(2.5, 'shilpa', 'Cake'),
(5, 'Shilpa ', 'Rajma'),
(5, 'Shilpa ', 'Rajma'),
(4, 'shilpa', 'Steak with lemon and capers'),
(5, 'shilpa', 'Turkey and Rice Stuffed Acorn Squash'),
(5, 'Shilpa ', 'French Vanilla Blackberry Pancakes'),
(2.5, 'Shilpa ', 'Biryani'),
(0, 'Shilpa ', 'Chicken 65 Burger'),
(0, 'shilpa', 'bhindi'),
(5, 'shilpa', 'BBQ Chicken'),
(5, 'guest', 'Cake'),
(4.5, 'guest', 'bhindi'),
(5, 'guest', 'Rajma'),
(1.5, 'guest', 'Beef Carrot Stew'),
(5, 'guest', 'Beef Carrot Stew');

-- --------------------------------------------------------

--
-- Table structure for table `tb_fr_recipes`
--

CREATE TABLE `tb_fr_recipes` (
  `id` int(11) NOT NULL,
  `recipe_name` varchar(50) NOT NULL,
  `ingredients` varchar(200) NOT NULL,
  `recipe_procedure` text NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `img_url` varchar(200) NOT NULL,
  `country_name` varchar(50) NOT NULL,
  `rating` float NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_fr_recipes`
--

INSERT INTO `tb_fr_recipes` (`id`, `recipe_name`, `ingredients`, `recipe_procedure`, `created_by`, `img_url`, `country_name`, `rating`) VALUES
(4, 'Curry ', 'onions', 'Add oil to a pan and heat it. Then add onions & chilli. Add chopped tomatoes, turmeric and salt. Sprinkle red chili powder, garam masala, coriander powder or curry powder. Very soon the masala begins to smell good, then drain the potatoes from the water and add to the pan.', 'parwinder', 'images/curry.jpg', 'Indian', 4.16667),
(7, 'Biryani', 'Rice,etc', 'Biryani, also known as biriyani, biriani, birani or briyani, is a mixed rice dish with its origins among the Muslims of the Indian subcontinent. It can be compared to mixing a curry, later combining it with semi-cooked rice separately. ', 'shilpa', 'images/biryani.jpg', 'Chinese', 3.83333),
(8, ' Vegetable Pulava', 'Onions,Tomatoes,Green Chilli', 'All Mixing fruit', 'parwinder', 'images/veg_rice.jpg', 'Indian', 4.75),
(9, 'Chicken 65 Burger', 'Chiken,Bread', 'All Mixing With Tomato with Saas', 'shilpa', 'images/burger.jpg', 'Chinese', 2.83333),
(11, 'Rajma', 'Rajma, tomoto, garlic, etc', 'It is a popular vegetarian.', 'shilpa', 'images/rajma.jpeg', 'Indian', 4.3),
(12, 'Cake', 'Egg,Flour,Baking Powder,Strawberry ', 'Preheat the oven to 350 degrees F (175 degrees C). Grease a fluted tube pan.Cream nonfat cream cheese, 1 cup plus 2 teaspoons butter, and sugar together in a bowl until light and fluffy, about 3 minutes. Add salt and vanilla extract and beat the mixture well. Add eggs, one at a time, beating thoroughly after each addition. Gradually add flour, one cup at a time, beating until just incorporated into the batter. Add strawberries after the last cup of flour and beat for a few seconds. Finish mixing with spatula if you need to.Pour batter into the prepared pan. Lightly sprinkle the top of the batter with the vanilla sugar.Bake in the preheated oven until a toothpick inserted into the center comes out clean, about 1 hour. Let cool in the pan. Invert carefully onto a serving plate or cooling rack. Let cool completely.', 'parwinder', 'images/cake.jpg', 'Indian', 3.875),
(41, 'Protein Balls', 'french vanilla cake mix,peanut butter,water,oats,sea salt,chocolate chips', 'The recipe Chocolate Chip Peanut Butter Protein Balls can be made in roughly 30 minutes. This recipe serves 25 and costs 6 cents per serving. One portion of this dish contains around 3g of protein, 6g of fat, and a total of 82 calories. A mixture of oats, peanut butter, water, and a handful of other ingredients are all it takes to make this recipe so tasty. It is brought to you by spoonacular user slimfast. It is a good option if you\'re following a dairy free diet.', 'parwinder', 'images/proteinballs.jpg', 'American', 4.5),
(42, 'French Vanilla Blackberry Pancakes', 'french vanilla cake mix,eggs,blackberries,slivered almonds,baking powder,sugar,water,butter', 'French Vanilla Blackberry Pancakes is a Mediterranean recipe that serves 12. One portion of this dish contains approximately 4g of protein, 6g of fat, and a total of 180 calories. For 29 cents per serving, this recipe covers 5% of your daily requirements of vitamins and minerals. Head to the store and pick up , sugar substitute, blackberries, and a few other things to make it today. It is brought to you by spoonacular user slimfast. It works well as a very reasonably priced morn meal. From preparation to the plate, this recipe takes around 20 minutes.', 'shilpa', 'images/pancake.jpg', 'French', 4.5),
(43, 'BBQ Chicken', 'chicken breasts,barbecue sauce,beef broth,light brown sugar,garlic powder,onion powder,cayenne pepper', 'Slow Cooker BBQ Chicken might be a good recipe to expand your main course recipe box. One portion of this dish contains roughly 25g of protein, 4g of fat, and a total of 333 calories. For $1.54 per serving, this recipe covers 15% of your daily requirements of vitamins and minerals. This recipe serves 6. This recipe from Pink When has 1 fans. It is a good option if you\'re following a gluten free and dairy free diet. Head to the store and pick up chicken breasts, onion powder, cayenne pepper, and a few other things to make it today. From preparation to the plate, this recipe takes around 4 hours and 10 minutes.', 'parwinder', 'images/bbq.jpg', 'American', 4.25),
(44, 'Keto Peppermint Hot Cocoa', 'liquid stevia,cocoa powder,chocolate mint,water', 'You can never have too many beverage recipes, so give Keto Peppermint Hot Cocoan a try. This recipe serves 1 and costs 92 cents per serving. Watching your figure? This gluten free, dairy free, paleolithic, and lacto ovo vegetarian recipe has 13 calories, 1g of protein, and 1g of fat per serving. From preparation to the plate, this recipe takes roughly 5 minutes. Head to the store and pick up liquid stevia, slimfast keto chocolate mint cup fat bomb, cocoa powder, and a few other things to make it today. Christmas will be even more special with this recipe. It is brought to you by spoonacular user slimfast.', 'shilpa', 'images/cocoa.jpg', 'Mexican', 5),
(45, 'Beef Carrot Stew', 'medium beef,chopped Scent leaves,carrots,curry paste', 'Scrape, wash and boil your carrots till soft.Place in a blender without water and grind till completely shredded.In a pot, heat up your oil and fry your onions, garlic and ginger.Pour in your Tatashe-pepper mix and your beef and allow to simmer on low heat for about 10-15 minutes.Season with curry, seasoning cubes and anything else you may wish to add.Pour in your carrots and stir in and allow to simmer on low heat for another 3-5 minutes.Add your chopped scent leaves and stir in and serve hot with rice, yam, plantain, pasta or any other accompaniment of your choice.', 'parwinder', 'images/beef.jpg', 'Thai', 3.5),
(46, 'Cod with Tomato-Olive-Chorizo Sauce and Mashed Pot', 'smoked sausage,salt,potatoes,plum tomatoes,olive oil,lemon juice,fresh parsley leaves', 'If frozen, thaw completely the cod fillets, and dry them with paper towels. season with salt and set aside.Peel and quarter the potatoes, and cook in lightly salted water until tender.Meanwhile, fry the pepper in olive oil for 3-4 minutes.Add cubed sausage, saut for another 2-3 minutes.Add the whole plum tomatoes,  halve them with a flat end of a wooden spoon.Stir in olives and parsley, cook the sauce gently for 2 minutes. Set aside.Drain the potatoes and season with parsley, olive oil and lemon juice. Use a hand masher to mix and mash. Leave in a warm place.Place the cod fillets into a hot oiled non-stick pan and cook over a high heat for 5 minutes on each side or until golden brown.Serve with mashed potatoes and sauce.', 'shilpa', 'images/cod.jpg', 'Malaysian', 4.5),
(47, 'Turkey and Rice Stuffed Acorn Squash', 'whole wheat bread crumbs,salt and pepper,bell pepper,vegeta seasoning,baby bella mushrooms,lean ground turkey,garlic,onion', 'Prepare rice according to package instructions. (I either cook the rice in chicken stock, or this time I used water and added 1 tablespoon vegeta seasoning to the water to add flavor to the rice as it cooks.)Meanwhile preheat oven to 375 degrees. Cut squash in half and drizzle with olive oil, salt and pepper. Roast skin side down for 30 minutes.Heat 1 tablespoon olive oil in large skilled. Add diced onions. Cook until translucent. Add garlic and cook for 1 minute longer. Add turkey, 1 tablespoon vegeta, red pepper, salt and pepper. Cook until turkey is browned and almost cooked through. Rough chop the mushrooms and add to the pan. Cook until mushrooms are softened. Add rice and spinach and stir until spinach is just wilted.Stuff the squash with the meat and rice mixture and top with bread crumbs. (Either toss bread crumbs with 2 teaspoons olive oil, or I just spray them with spray olive oil to make sure that they brown in the oven.Bake stuffed squash in oven until bread crumbs are browned.', 'parwinder', 'images/turkey.jpg', 'Malaysian', 4.5),
(48, 'Steak with lemon and capers', 'lemon wedges,steak,capers', 'Flatten the steaks lightly with a meat mallet.Combine flour, salt and pepper, dip steaks into the mixture, coating both sides.Saut the steaks in a hot mixture of butter and olive oil over medium heat, about 4 minutes on each side. Remove from the pan and set aside.Saut the garlic in the same fat for about 1 minute, stirring.Add the wine and lemon juice, stir and simmer for 5 minutes to slightly reduce the liquid.Add capers, stir. Return the steaks to the pan, cover and simmer over low heat for 4 minutes.', 'shilpa', 'images/steak.jpg', 'American', 3.5),
(49, 'Gluten Free Onion Rings', 'gluten free all purpose flour,vinegar,oil,egg,milk,onion', 'Mix egg, oil, and milk on low speed of mixer for 1 minute.Add Bette\'s Mix (or equivalent dry ingredients) and mix until smooth.Put 1/2 cup all purpose gf flour in a shallow dish. Coat onion rings in flour.Dip floured onions in prepared batter.Fry in hot oil (375 degrees, at least 1-inch deep) until desired shade of brown.', 'parwinder', 'images/onionrings.jpg', 'Thai', 4.5),
(50, 'Rhubarb Cake with Meringue', 'egg whites,baking powder,almonds,flour', 'Preheat the oven to 350F Convection. Grease a round 26 cm Spring pan (9 1/2 inch).Wash, dry and peel the rhubarb. Cut it in little pieces, mix with 2 tablespoon of sugar and let sit for at least 1/2 hour. It will extract a lot of water that needs to be drained. Pat rhubarb dry for further use.In a kitchen machine beat together butter, sugar and vanilla extract until the butter is fluffy and the sugar is dissolved. Put in the eggs, one at a time and mix well.In a separate bowl sift together flour, ground almonds, salt and baking powder, add slowly to the egg mixture. Dont over mix.Fill dough into the spring pan, top with dried rhubarb and bake for 25 min.In the mean time prepare the meringue baiser topping. Beat egg whites until stiff peaks form. Slowly add the sugar until meringue is firm and shiny.Spread the meringue evenly over the rhubarb and decorate with almond slices. Return to the oven for another 15 min. Cover the cake with aluminum foil after 5 min. in case the meringue does turn too dark.Cool completely before removing the cake from the pan.', 'shilpa', 'images/caakee.jpg', 'Thai', 4.5),
(72, 'bhindi', 'garlic, ginger, lady finger 200g', 'Take a pan and put one tb oil \n', 'shilpa', 'images/00000000261061051545179868935.jpg', 'Indian', 2.25);

-- --------------------------------------------------------

--
-- Table structure for table `tb_fr_user_registration`
--

CREATE TABLE `tb_fr_user_registration` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `emailid` varchar(50) NOT NULL,
  `uname` varchar(50) NOT NULL,
  `pwd` varchar(50) NOT NULL,
  `profile_photo` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_fr_user_registration`
--

INSERT INTO `tb_fr_user_registration` (`id`, `name`, `phone`, `emailid`, `uname`, `pwd`, `profile_photo`) VALUES
(1, 'testing ', '134567890', 'gagga@gmal.com', 'test', 'test123', ''),
(2, 'chech', '12345436755', 'chek@123', 'check', 'check123', ''),
(5, 'test1', '1234567890', 'fagsg123', 'test1', '123', ''),
(7, 'ivar', '8989', 'ravibabu89.nadakuditi@gmail.com', '123', '123', ''),
(8, 'shilpa', '5145913434', 'shilpasharma0123@gmail.com', 'shilpa', 'Welcome1', 'profile/foodrecipe_20200301_213733.png'),
(11, 'parwinder', '124589', 'parwinder@test.com', 'parwinder', '123', 'profile/foodrecipe_20200301_111752.png'),
(29, 'rishabh', '7474224', 'rishabh', 'rishabh', 'rishabh', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_fr_category`
--
ALTER TABLE `tb_fr_category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_fr_recipes`
--
ALTER TABLE `tb_fr_recipes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_fr_user_registration`
--
ALTER TABLE `tb_fr_user_registration`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uname` (`uname`),
  ADD UNIQUE KEY `emailid` (`emailid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_fr_category`
--
ALTER TABLE `tb_fr_category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `tb_fr_recipes`
--
ALTER TABLE `tb_fr_recipes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- AUTO_INCREMENT for table `tb_fr_user_registration`
--
ALTER TABLE `tb_fr_user_registration`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
